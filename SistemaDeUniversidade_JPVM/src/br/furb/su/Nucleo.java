/**
 * SU - Sistema Universitário 
 */
package br.furb.su;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.furb.su.dataset.InDataset;
import br.furb.su.dataset.OutDataset;
import br.furb.su.dataset.reader.AlunosReader;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DataReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.dataset.writer.DataWriter;
import br.furb.su.dataset.writer.DiplomasWriter;
import br.furb.su.dataset.writer.EstatisticasWriter;
import br.furb.su.dataset.writer.MensagensWriter;
import br.furb.su.dataset.writer.MensalidadesWriter;
import br.furb.su.modelo.Mensagem;
import br.furb.su.modelo.dados.Aluno;
import br.furb.su.modelo.dados.Curso;
import br.furb.su.modelo.dados.Diploma;
import br.furb.su.modelo.dados.Disciplina;
import br.furb.su.modelo.dados.Historico;
import br.furb.su.modelo.dados.Mensalidade;
import br.furb.su.modelo.dados.SituacaoDisciplina;
import br.furb.su.modelo.dados.SolicitacaoDiploma;
import br.furb.su.modelo.dados.SolicitacaoMatricula;

public class Nucleo {

	private static final String VERIFICACAO_MENSALIDADES = "verificação de mensalidades";
	private static final String EMISSAO_DIPLOMA = "emissão de diploma";
	private static final String SOLICITACAO_MATRICULA = "solicitação de matrícula";

	public static void main(String[] args) throws IOException {
		carregarDados();
		InDataset inDataset = Sistema.inDataset();
		OutDataset outDataset = Sistema.outDataset();

		ligarDados(inDataset);
		processar(inDataset, outDataset);
		gravarDados(outDataset);
	}

	private static void lerDados(InDataset inDataset) throws FileNotFoundException {
		Sistema.debug("lendo dados");
		File pastaEntrada = Sistema.getPastaEntrada();
		DataReader<?>[] leitores = new DataReader[] { new AlunosReader(pastaEntrada), //
				new MensalidadesReader(pastaEntrada), //
				new SolicitacoesMatriculaReader(pastaEntrada), //
				new HistoricosReader(pastaEntrada), //
				new DisciplinasReader(pastaEntrada), //
				new CursoReader(pastaEntrada), //
				new SolicitacoesDiplomaReader(pastaEntrada) //
		};
		for (int i = 0; i < leitores.length; i++) {
			leitores[i].ler(inDataset);
		}
		Sistema.debug("leu dados");
	}

	private static void ligarDados(InDataset dataset) {
		Sistema.debug("ligando dados");
		ligarMensalidadeAlunos(dataset);
		ligarSolicDiplomaAlunos(dataset);
		ligarSolicMatriculaAlunos(dataset);
		ligarHistoricos(dataset);
		ligarCursoDisciplina(dataset);
		Sistema.debug("dados ligados");
	}

	private static void ligarMensalidadeAlunos(InDataset dataset) {
		Sistema.debug("ligando mensalidades <-> alunos");
		List<Mensalidade> mensalidades = dataset.getMensalidades();
		int qtMensalidades = mensalidades.size();
		for (int i = 0; i < qtMensalidades; i++) {
			Mensalidade mensalidade = mensalidades.get(i);
			Aluno aluno = dataset.findAluno(mensalidade.getCodAluno());
			aluno.addMensalidade(mensalidade);
			mensalidade.setAluno(aluno);
		}
		Sistema.debug("ligou mensalidades <-> alunos");
	}

	private static void ligarSolicDiplomaAlunos(InDataset dataset) {
		Sistema.debug("ligando solicitacoes_diploma <-> alunos");
		List<SolicitacaoDiploma> solicitacoes = dataset.getSolicitacoesDiploma();
		int qtSolicitacoes = solicitacoes.size();
		for (int i = 0; i < qtSolicitacoes; i++) {
			SolicitacaoDiploma solic = solicitacoes.get(i);
			Aluno aluno = dataset.findAluno(solic.getCodAluno());
			if (aluno == null) {
				Sistema.debug("aluno null");
				// TODO: não seria uma exceção?
			}
			solic.setAluno(aluno);
			solic.setCurso(dataset.getCurso(solic.getCodCurso()));
		}
		Sistema.debug("ligou solicitacoes_diploma <-> alunos");
	}

	private static void ligarSolicMatriculaAlunos(InDataset dataset) {
		Sistema.debug("ligando solicitacoes_matricula <-> alunos");
		List<SolicitacaoMatricula> solicitacoes = dataset.getSolicitacoesMatricula();
		int qtSolicitacoes = solicitacoes.size();
		for (int i = 0; i < qtSolicitacoes; i++) {
			SolicitacaoMatricula solic = solicitacoes.get(i);
			Aluno aluno = dataset.findAluno(solic.getCodAluno());
			solic.setAluno(aluno);
			solic.setDisciplina(dataset.getDisciplina(solic.getCodDisciplina()));
			solic.setCurso(dataset.getCurso(solic.getCodCurso()));
		}
		Sistema.debug("ligou solicitacoes_matricula <-> alunos");
	}

	private static void ligarHistoricos(InDataset dataset) {
		Sistema.debug("ligando históricos");
		List<Historico> historicos = dataset.getHistoricos();
		int qtHistoricos = historicos.size();
		for (int i = 0; i < qtHistoricos; i++) {
			Historico historico = historicos.get(i);
			Aluno aluno = dataset.findAluno(historico.getCodAluno());
			aluno.addHistorico(historico);
			historico.setAluno(aluno);
			historico.setDisciplina(dataset.getDisciplina(historico.getCodDisciplina()));
			historico.setCurso(dataset.getCurso(historico.getCodCurso()));
		}
		Sistema.debug("ligou históricos");
	}

	private static void ligarCursoDisciplina(InDataset dataset) {
		Sistema.debug("ligando cursos <-> disciplina");
		List<Curso> cursos = new ArrayList<>(dataset.getCursosMap().values());
		Map<Integer, Disciplina> disciplinas = dataset.getDisciplinasMap();

		int qtCursos = cursos.size();
		for (int i = 0; i < qtCursos; i++) {
			cursos.get(i).resolver(disciplinas);
		}
		Sistema.debug("ligou cursos <-> disciplina");
	}

	private static void processar(InDataset inDataset, OutDataset outDataset) {
		Sistema.debug("iniciando processamento");
		processaMensalidadesAtrasadas(inDataset, outDataset);
		processaDiplomas(inDataset, outDataset);
		processaMatriculas(inDataset, outDataset);
		processaNovasMensalidades(inDataset, outDataset);
		Sistema.debug("processamento concluído");
	}

	private static void processaMensalidadesAtrasadas(InDataset inDataset, OutDataset outDataset) {
		Sistema.debug("processando mensalidades");
		List<Mensalidade> mensalidades = inDataset.getMensalidades();
		Calendar dataAtual = Sistema.getDataAtual();
		int qtMensalidades = mensalidades.size();
		double totalMulta = 0;
		for (int i = 0; i < qtMensalidades; i++) {
			Mensalidade mensalidade = mensalidades.get(i);
			if (!mensalidade.isPaga() && dataAtual.after(mensalidade.getVencimento())) {
				totalMulta += mensalidade.calculaMulta();
				outDataset.addAviso(new Mensagem(mensalidade.getAluno(), VERIFICACAO_MENSALIDADES, //
						String.format("A mensalidade do dia %s está atrasada e gerou multa de R$ %.2f", formatarData(mensalidade.getCompetencia()), totalMulta)));
			}
		}
		Sistema.estatisticas().financeiras().setTotalMulta(totalMulta);
		Sistema.debug("mensalidades processadas");
	}

	private static void processaDiplomas(InDataset inDataset, OutDataset outDataset) {
		Sistema.debug("processando diplomas");
		List<SolicitacaoDiploma> solicitacoes = inDataset.getSolicitacoesDiploma();
		int i = 0;
		int qtSolicitacoes = solicitacoes.size();
		for (i = 0; i < qtSolicitacoes; i++) {
			boolean emissaoNegada = false;
			SolicitacaoDiploma solic = solicitacoes.get(i);
			Aluno aluno = solic.getAluno();
			if (aluno.possuiMensalidadeAtrasada()) {
				outDataset.addProblema(new Mensagem(aluno, EMISSAO_DIPLOMA, "Não é possível emitir o diploma pois há mensalidade(s) atrasada(s)."));
			} else {
				Curso curso = solic.getCurso();
				List<Historico> historicos = curso.filtrarHistoricos(aluno.getHistoricos());
				if (historicos.size() < curso.getDisciplinas().size()) {
					outDataset.addProblema(new Mensagem(aluno, EMISSAO_DIPLOMA, "Não é possível emitir o diploma pois há disciplinas não cursadas."));
				} else {
					Iterator<Historico> iterator = historicos.iterator();
					while (iterator.hasNext()) {
						Historico historico = iterator.next();
						if (historico.getSituacao() != SituacaoDisciplina.APROVADO) {
							outDataset.addProblema(new Mensagem(aluno, EMISSAO_DIPLOMA, "Não é possível emitir o diploma pois não há aprovação em todas as disciplinas do curso."));
							emissaoNegada = true;
							break;
						}
					}
					if (!emissaoNegada) {
						Diploma diploma = new Diploma(aluno, solic.getCurso(), Sistema.getDataAtual());
						outDataset.addDiploma(diploma);
					}
				}
			}
		}
		Sistema.debug("diplomas processados");
	}

	private static void processaMatriculas(InDataset inDataset, OutDataset outDataset) {
		Sistema.debug("processando matriculas");
		List<SolicitacaoMatricula> solicitacoes = inDataset.getSolicitacoesMatricula();
		List<Historico> historicos = outDataset.getHistoricos();
		int qtSolicitacoes = solicitacoes.size();
		for (int i = 0; i < qtSolicitacoes; i++) {
			SolicitacaoMatricula solic = solicitacoes.get(i);
			Aluno aluno = solic.getAluno();
			Disciplina disciplina = solic.getDisciplina();
			if (aluno.possuiMensalidadeAtrasada()) {
				outDataset.addProblema(new Mensagem(aluno, SOLICITACAO_MATRICULA, "Não é possível realizar matrículas pois há mensalidade(s) em atraso."));
			} else {
				if (aluno.cursou(disciplina)) {
					outDataset.addAviso(new Mensagem(aluno, SOLICITACAO_MATRICULA, "Esta disciplina já foi cursada."));
				}
				Historico historico = new Historico(aluno, disciplina, solic.getCurso(), SituacaoDisciplina.MATRICULADO, Sistema.getDataAtual());
				historicos.add(historico);
			}
		}
		Sistema.debug("matrículas processadas");
	}

	private static void processaNovasMensalidades(InDataset inDataset, OutDataset outDataset) {
		Sistema.debug("processando novas mensalidades");
		List<Aluno> alunos = Sistema.filtraAlunosAtivos(inDataset);
		Calendar dataAtual = Sistema.getDataAtual();
		double totalMensalidades = 0;
		double totalMatriculas = 0;
		int i;
		int qtAlunos = alunos.size();

		for (i = 0; i < qtAlunos; i++) {
			int mensalidadeAluno = 0;
			int matriculaAluno = 0;
			Aluno aluno = alunos.get(i);
			List<Historico> historicos = aluno.getHistoricosAtivos();
			Iterator<Historico> iterator = historicos.iterator();
			while (iterator.hasNext()) {
				Historico historico = iterator.next();
				final Disciplina disciplina = historico.getDisciplina();
				if (historico.getSituacao() == SituacaoDisciplina.MATRICULADO) {
					matriculaAluno += disciplina.getValorMatricula();
				} else {
					mensalidadeAluno += disciplina.getValorMensal();
				}
			}
			Mensalidade mensalidade = new Mensalidade(aluno, mensalidadeAluno + matriculaAluno, dataAtual, Sistema.proxVecto(dataAtual), false);
			outDataset.addMensalidade(mensalidade);
			totalMatriculas += matriculaAluno;
			totalMensalidades += mensalidadeAluno;
		}
		Estatisticas.Financeiras financeiras = Sistema.estatisticas().financeiras();
		financeiras.setTotalProxMensalidades(totalMensalidades);
		financeiras.setTotalMatriculas(totalMatriculas);
		Sistema.debug("novas mensalidades processadas");
	}

	private static void gravarDados(OutDataset outDataset) throws IOException {
		Sistema.debug("iniciando gravacao de dados");
		File pastaSaida = Sistema.getPastaSaida();
		DataWriter<?>[] gravadores = new DataWriter[] { new MensagensWriter(pastaSaida), //
				new MensalidadesWriter(pastaSaida), //
				new DiplomasWriter(pastaSaida), //
				new EstatisticasWriter(pastaSaida), //
		// ...
		};

		for (int i = 0; i < gravadores.length; i++) {
			gravadores[i].write(outDataset);
		}
		Sistema.debug("dados gravados");
	}

	private static String formatarData(Calendar data) {
		return String.valueOf(data.get(Calendar.YEAR)).concat("/").concat(String.valueOf(data.get(Calendar.MONDAY)));
	}

	private static Calendar converterData(String string) {
		Matcher m = Pattern.compile("(\\d{2})/(\\d{4})").matcher(string);
		if (!m.matches()) {
			throw new IllegalArgumentException("data não reconhecida: " + string);
		}
		int mes = Integer.parseInt(m.group(1));
		int ano = Integer.parseInt(m.group(2));
		Calendar data = Calendar.getInstance();
		data.set(ano, mes - 1, 1, 0, 0, 0);
		return data;
	}

	public static void carregarDados() {
		Sistema.debug("requisitando parâmetros");
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			System.out.print("Informe a pasta de origem dos dados: ");
			File pastaOrigem = new File(scanner.nextLine());

			System.out.print("Informe a pasta de destino dos dados: ");
			File pastaDestino = new File(scanner.nextLine());

			System.out.print("Informe a data atual no format 'MM/AAAA': ");
			Calendar dataAtual = converterData(scanner.nextLine());

			Sistema.load(pastaOrigem, pastaDestino, dataAtual);
		} finally {
			scanner.close();
		}
		try {
			lerDados(Sistema.inDataset());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("não foi possível carregar os dados");
		}
	}

}
