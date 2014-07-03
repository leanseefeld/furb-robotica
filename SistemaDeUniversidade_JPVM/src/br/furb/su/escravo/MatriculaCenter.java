package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import jpvm.jpvmBuffer;
import jpvm.jpvmException;
import jpvm.jpvmMessage;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.modelo.dados.SolicitacaoMatricula;
import br.furb.su.operacoes.Operacao;
import br.furb.su.operacoes.OperacaoFactory;

public class MatriculaCenter extends EscravoBase {

	private final Collection<SolicitacaoMatricula> sols = new ArrayList<SolicitacaoMatricula>();
	private final SolicitacoesMatriculaReader reader = new SolicitacoesMatriculaReader();
	private jpvmTaskId mensalidadeCenter;
	private jpvmTaskId cursoCenter;

	public MatriculaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) throws jpvmException {
		new MatriculaCenter().run();
	}

	@Override
	protected void doUpload(String buffer) {
		sols.addAll(reader.ler(buffer));
		tryResponder(ResponseEscravo.OK, null);
	}

	@Override
	protected void doDownload(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doGet(String buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetSlave(String slaveName, jpvmTaskId taskId) {
		if (slaveName.equals("mensalidadeCenter")) {
			mensalidadeCenter = taskId;
		} else if (slaveName.equals("cursoCenter")) {
			cursoCenter = taskId;
		}
		try {
			responder(ResponseEscravo.OK, null);
		} catch (jpvmException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doOperation(Operacao op) {
		if (op.getNome().equals(OperacaoFactory.NOME_PROCESS_MATRICULAS)) {
			Collection<Mensagem> mensagens = new ArrayList<>();
			Calendar dataAtual = (Calendar) op.getParam("dataAtual");
			for (SolicitacaoMatricula solic : sols) {
				long aluno = solic.getCodAluno();
				int disciplina = solic.getCodDisciplina();
				if (alunoPossuiAtraso(aluno)) {
					mensagens.add(new Mensagem(aluno, "Não é possível realizar matrículas pois há mensalidade(s) em atraso."));
				} else {
					if (alunoCursou(aluno, disciplina)) {
						mensagens.add(new Mensagem(aluno, "Esta disciplina já foi cursada."));
					}
					Historico historico = new Historico(aluno, disciplina, solic.getCodCurso(), SituacaoDisciplina.MATRICULADO, dataAtual);
					addHistorico(historico);
				}
			}
		} else {
			tryResponder(ResponseEscravo.FAILURE, String.format(MSG_OP_NAO_RECONHECIDO, op.getNome()));
		}
	}

	private boolean alunoPossuiAtraso(long aluno) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean alunoCursou(long aluno, int disciplina) {
		// TODO Auto-generated method stub
		return false;
	}

	private void addHistorico(Historico historico) {
		// TODO Auto-generated method stub

	}

}
