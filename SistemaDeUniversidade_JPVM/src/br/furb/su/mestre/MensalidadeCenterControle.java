package br.furb.su.mestre;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jpvm.jpvmBuffer;
import jpvm.jpvmEnvironment;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.Estatisticas;
import br.furb.su.Sistema;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.writer.MensalidadesWriter;
import br.furb.su.escravo.MensalidadeCenter;
import br.furb.su.escravo.RequestEscravo;
import br.furb.su.modelo.dados.Aluno;
import br.furb.su.modelo.dados.Mensalidade;
import br.furb.su.operacoes.OperacaoFactory;

public class MensalidadeCenterControle extends BaseCenterControle {

	private final MensalidadesReader mensReader = new MensalidadesReader();
	private final MensalidadesWriter mensWriter = new MensalidadesWriter();

	public MensalidadeCenterControle(jpvmEnvironment pvm, jpvmTaskId tid) {
		super(pvm, tid);
	}

	public List<Mensalidade> getMensalidade(int codAluno) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", //
				MensalidadeCenter.GET_MENSALIDADE, // 
				MensalidadeCenter.PARAM_COD_ALUNO, codAluno));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		final String bufferStr = msg.buffer.upkstr();
		checkErrorResponse(msg);
		return mensReader.ler(bufferStr);
	}

	public void insereMensalidade(Mensalidade m) throws jpvmException {
		StringBuilder comando = new StringBuilder();
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			mensWriter.gravarDados(Arrays.asList(m), pw);
			comando.append(sw.getBuffer().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(comando.toString());
		pvm.pvm_send(buffer, tid, RequestEscravo.UPLOAD.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
	}

	public void processaMensalidadesAtrasadas() throws jpvmException {
		async_enviaOperacao(OperacaoFactory.processarMensalidadesAtrasadas(Sistema.getDataAtual()));
	}

	public void processarNovasMensalidades(List<Aluno> alunosAtivos, Calendar dataAtual) throws jpvmException {
		String strAlunos;
		{
			StringBuilder sb = new StringBuilder();
			Iterator<Aluno> it = alunosAtivos.iterator();
			boolean hasNext = it.hasNext();
			while (hasNext) {
				sb.append(it.next().getCod());
				hasNext = it.hasNext();
				if (hasNext) {
					sb.append(';');
				}
			}
			strAlunos = sb.toString();
		}
		async_enviaOperacao(OperacaoFactory.processarNovasMensalidades(strAlunos, dataAtual));
	}

	public boolean alunoPossuiAtraso(long aluno) throws jpvmException {
		jpvmBuffer buffer = new jpvmBuffer();
		buffer.pack(String.format("%s\n%s=%d", // 
				MensalidadeCenter.GET_ALUNO_POSSUI_ATRASO, // 
				MensalidadeCenter.PARAM_COD_ALUNO, aluno));
		pvm.pvm_send(buffer, tid, RequestEscravo.GET.tag());
		jpvmMessage msg = pvm.pvm_recv(tid);
		checkErrorResponse(msg);
		return Boolean.valueOf(msg.buffer.upkstr());
	}

	public void setCursoCenter(jpvmTaskId taskId) throws jpvmException {
		super.setEscravo(taskId, "cursoCenter");
	}

	public Collection<Mensalidade> downloadMensalidades() throws jpvmException {
		super.requestDownload("mensalidades");
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);
		return mensReader.ler(msg.buffer.upkstr());
	}

	public Estatisticas downloadEstatisticas() throws jpvmException {
		super.requestDownload("estatisticas");
		jpvmMessage msg = pvm.pvm_recv();
		checkErrorResponse(msg);

		jpvmBuffer buffer = msg.buffer;
		int bytesCount = buffer.upkint();
		byte[] bytes = new byte[bytesCount];
		buffer.unpack(bytes, bytesCount, 1);

		Estatisticas es;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
			es = (Estatisticas) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Não foi possível recuperar as estatísticas");
		}
		return es;
	}

}
