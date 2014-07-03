package br.furb.su.escravo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import jpvm.jpvmException;
import jpvm.jpvmTaskId;
import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.mestre.CursoCenterControle;
import br.furb.su.mestre.MensalidadeCenterControle;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.modelo.dados.SolicitacaoMatricula;
import br.furb.su.operacoes.Operacao;
import br.furb.su.operacoes.OperacaoFactory;

public class MatriculaCenter extends EscravoBase {

	private final Collection<SolicitacaoMatricula> sols = new ArrayList<SolicitacaoMatricula>();
	private final SolicitacoesMatriculaReader reader = new SolicitacoesMatriculaReader();
	private MensalidadeCenterControle mensalidadeCenter;
	private CursoCenterControle cursoCenter;

	public MatriculaCenter() throws jpvmException {
		super();
	}

	public static void main(String[] args) throws jpvmException {
		new MatriculaCenter().run();
	}

	@Override
	protected void doUpload(String buffer) {
		sols.addAll(reader.ler(buffer));
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doSetSlave(String slaveName, jpvmTaskId taskId) {
		if (slaveName.equals("mensalidadeCenter")) {
			mensalidadeCenter = new MensalidadeCenterControle(pvm, taskId);
		} else if (slaveName.equals("cursoCenter")) {
			cursoCenter = new CursoCenterControle(pvm, taskId);
		}
		tryResponder(ResponseEscravo.OK);
	}

	@Override
	protected void doOperation(Operacao op) {
		if (op.getNome().equals(OperacaoFactory.NOME_PROCESS_MATRICULAS)) {
			processaMatriculas(op);
		} else {
			tryResponder(ResponseEscravo.FAILURE, String.format(MSG_OP_NAO_RECONHECIDA, op.getNome()));
		}
	}

	private void processaMatriculas(Operacao op) {
		Calendar dataAtual = (Calendar) op.getParam("dataAtual");
		for (SolicitacaoMatricula solic : sols) {
			long aluno = solic.getCodAluno();
			int disciplina = solic.getCodDisciplina();
			boolean alunoPossuiAtraso;
			try {
				alunoPossuiAtraso = mensalidadeCenter.alunoPossuiAtraso(aluno);
			} catch (jpvmException e) {
				throw new RuntimeException("Não foi possível se comunicar com o servidor de mensalidades", e);
			}
			if (alunoPossuiAtraso) {
				mensagens.add(new Mensagem(aluno, "Não é possível realizar matrículas pois há mensalidade(s) em atraso."));
			} else {
				try {
					if (cursoCenter.alunoCursou(aluno, disciplina)) {
						mensagens.add(new Mensagem(aluno, "Esta disciplina já foi cursada."));
					}
					Historico historico = new Historico(aluno, disciplina, solic.getCodCurso(), SituacaoDisciplina.MATRICULADO, dataAtual);
					cursoCenter.insereHistorico(historico);
				} catch (jpvmException e) {
					throw new RuntimeException("Não foi possível se comunicar com o servidor de históricos", e);
				}
			}
		}
		tryResponder(ResponseEscravo.OK);
	}

}
