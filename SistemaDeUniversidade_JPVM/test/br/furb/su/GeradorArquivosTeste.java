package br.furb.su;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import br.furb.su.dataset.reader.AlunosReader;
import br.furb.su.dataset.reader.CursoReader;
import br.furb.su.dataset.reader.DisciplinasReader;
import br.furb.su.dataset.reader.HistoricosReader;
import br.furb.su.dataset.reader.MensalidadesReader;
import br.furb.su.dataset.reader.SolicitacoesDiplomaReader;
import br.furb.su.dataset.reader.SolicitacoesMatriculaReader;
import br.furb.su.modelo.dados.SituacaoDisciplina;

public class GeradorArquivosTeste {

	private static final SituacaoDisciplina[] SITUACOES_DISCIPLINAS = SituacaoDisciplina.values();

	private static final Random RANDOM = new Random();

	private static final String[] NOMES_CURSOS = { "Administração", "Agronomia", "Arquitetura E Urbanismo",
			"Biblioteconomia", "Ciências Biológicas", "Ciências Contábeis", "Ciências Da Computação",
			"Ciências Econômicas", "Ciências Sociais", "Comunicação E Expressão Visual", "Direito", "Educação Física",
			"Enfermagem", "Engenharia De Controle E Automação Industrial", "Engenharia Civil",
			"Engenharia De Alimentos", "Engenharia De Aqüicultura", "Engenharia De Materiais",
			"Engenharia De Produção Civil", "Engenharia De Produção Elétrica", "Engenharia De Produção Mecânica",
			"Engenharia Elétrica", "Engenharia Mecânica", "Engenharia Química", "Engenharia Sanitária E Ambiental",
			"Farmácia", "Filosofia", "Física", "Geografia", "História", "Jornalismo", "Letras", "Matemática",
			"Medicina", "Nutrição", "Odontologia", "Pedagogia", "Psicologia", "Química", "Serviço Social",
			"Sistemas De Informação" };
	private static final String[] NOMES_DISCIPLINAS = { "Educação Física – Prática Desportiva I",
			"Introdução a Computação", "Introdução a Programação", "Universidade Ciência e Pesquisa",
			"Arquitetura de Computadores I", "Fundamentos Matemáticos", "Introdução a Computação",
			"Introdução a Programação", "Modelagem Orientada a Objetos", "Álgebra Linear para Computação",
			"Arquitetura de Computadores", "Linguagem Científica", "Lógica para Computação", "Prática Desportiva II",
			"Programação Orientada a Objetos I", "Álgebra Linear para Computação", "Arquitetura de Computadores",
			"Linguagem Científica", "Lógica para Computação", "Prática Desportiva II",
			"Programação Orientada a Objetos I", "Algoritmos e Estruturas de Dados",
			"Estatística Aplicada à Informática", "Programação Orientada a Objetos II", "Sistemas Operacionais",
			"Teoria da Computação", "Algoritmos e Estruturas de Dados", "Estatística Aplicada à Informática",
			"Programação Orientada a Objetos II", "Sistemas Operacionais", "Teoria da Computação",
			"Desafios Sociais e Contemporâneos", "Linguagens de Programação", "Linguagens Formais",
			"Métodos Quantitativos", "Protocolos de Comunicação de Dados", "Teoria dos Grafos", "Banco de Dados I",
			"Compiladores", "Desenvolvimento de Aplicações Concorrentes e Distribuídas", "Engenharia de Software",
			"Redes de Computadores", "Banco de Dados II", "Comportamento Organizacional", "Disciplina Optativa I",
			"Disciplina Optativa I", "Processo de Software I", "Sistemas Distribuídos", "Banco de Dados II",
			"Comportamento Organizacional", "Disciplina Optativa I", "Processo de Software I", "Sistemas Distribuídos",
			"Computação Gráfica", "Desenvolvimento para Web", "Disciplina Optativa II", "Inteligência Artificial",
			"Processo de Software II", "Disciplina Optativa III", "Disciplina Optativa IV",
			"Empreendedor em Informática", "Legislação em Informática", "Sistemas Multimídia",
			"Trabalho de Conclusão de Curso I", "Trabalho de Conclusão de Curso II" };
	private static final String[] NOMES_ALUNOS = { "ABIGAIL BERNARDO PRANGE", "ADEMIR ATUI",
			"ADRIANA CRISTINA FURTADO", "ADRIANO CASCAES", "ADRIANO RAMON DE ANDRADE BANCZYNSKI",
			"AELSON NOGUEIRA RUIVO", "ALEXANDRE KLETTIMBERG", "ALINE MARIZA PINHEIRO SOLER",
			"ALLAN JONES VIEIRA DA SILVA", "AMÁBILE DOS SANTOS ZIRBEL", "AMANDA FISCHER", "ANA CAROLINE ESTRAICH",
			"ANA PAULA DE OLIVEIRA", "ANDERSON ALAMBEC", "ANDERSON DIEGO FERREIRA", "ANDERSON LUIZ DA ROCHA CARNEIRO",
			"ANDERSON SOTHE WINKLER", "ANDRÉ DIX", "ANDRÉ FELIPE HEMKMAIER", "ANDRÉ PHILIP STARUCKA",
			"ANDREANI MACEDO DE LIZ", "ANDREI MARCELO DE FRANÇA MORAES", "ANDRESSA SCHULZ", "ANDREZA MARA LOPES",
			"ANNE CAROLINE SCHIMAIDA", "ANTONIO CARLOS MACHADO JUNIOR", "ANTÔNIO GONÇALVES DOS SANTOS NETO",
			"ANTONIO RAFAEL FAGUNDES MATOS", "ARIELA MACHADO KOCH", "AROLDO DE SOUSA ARAÚJO FILHO",
			"AUDIO APARECIDO FRANÇA CANDIDO", "AUGUSTO VIRGILI WILLECKE", "BÁRBARA CECHETTO", "BEATRIZ SOUZA",
			"BLENER FELIPE DA SILVA VALINDOLFO", "BRUNA KNOTH", "BRUNO HENRIQUE FREIBERGER", "BRUNO PHILIPPE BLAU",
			"BRUNO RIBEIRO", "CAMILA DE SIQUEIRA", "CAMILA GABRIELA PACHECO", "CARINE CONSTANTE BASTOS",
			"CARINE KWITSCHAL SOUZA", "CARLA MORAIS CAMARGO", "CARLOS ANTÔNIO SEBOLD", "CARLOS ROBERTO MORETTO JUNIOR",
			"CAROLINA LOPES DUARTE", "CAROLINE CONSTANTE BASTOS", "CAROLINE DE SOUSA LOPES HEIDEN",
			"CESAR AUGUSTO SIQUEIRA JUNIOR", "CHARLES EUDARDO LEAL", "CLAITON MATEUS CARNEIRO.",
			"CLEDSON FELIPE COLZANI", "CLEITON FRANCELINO", "CLEITON ROBERTO ESTRAICH", "CRISTIAN MARQUES LEICHT",
			"CRISTIAN THEISS", "CRISTIANE MIRIN SCOTTI", "CRISTINA CORREA DA SILVA", "DANIEL CARLOS FISCHER",
			"DANIEL DA SILVA", "DANIEL FELIPE SOARES", "DANIEL MARTINS", "DANIEL MENDES DA SILVA SALVIATO",
			"DANIEL RODRIGO KORC", "DANIEL RODRIGUES ZEREDO", "DANIELA CORREA DA SILVA",
			"DANILO GONÇALVES DE LIMA PACHECO", "DANILSO ALVES DE SOUZA", "DELTON GIANNI CAETANO MOSSA",
			"DENIS FABIANO ZIMMERMANN", "DENIS WILLIAM FERNANDES", "DENIZE BANDEIRA REQUIEL", "DIEGO ARMANDO CACILHA",
			"DIEGO BORGES FERREIRA", "DIEGO CUGIKI", "DIEGO JACKSON BATISTA", "DIOGO EISING", "DOUGLAS ANDRÉ BUDKE",
			"DOUGLAS JOSÉ DIEL", "DOUGLAS JUNGES", "DOUGLAS OTTEQUIR", "DOUGLAS REALDINO CONTI", "DOUGLAS STOLARCZH",
			"EDSON KROENKE JUNIOR", "EDUARDO FELIPE RODRIGUES", "EDUARDO NILSEN", "EDUARDO VOLTOLINI",
			"EDUARDO WILLIAN GODOZ", "ELIZANDRA RIBEIRO", "ELIZIANE DE SOUSA VIEIRA",
			"ELVERCIO HENRIQUE GOMES DE LIMA", "ELZA ORTIZ DOS PASSOS", "EMERSON MESKAU DA CRUZ", "EVERSON DIAS",
			"EVERTON DA SILVA", "FÁBIO ANDRÉ KANNENBERG", "FABIO DE ESPINDOLA", "FAGNER LUCAS DE MELLO",
			"FELIPE ANZINI DE SOUZA", "FELIPE WEHMUTH", "FERNANDA CABRAL", "FERNANDA CAROLINA WILVERT",
			"FERNANDA DE SOUZA E SILVA", "FERNANDO ALFONSO MANSO BOLO", "FILIPE FRANZ BARRETO",
			"FLAVIO ANDRE DE BORBA", "FRANCIELE LARISSA DE MELO", "FRANCIEUDO SILVA DE OLIVEIRA",
			"FRANCISCO KRAUTCHUK NETO", "FRANCISCO MACHADO JUNIOR", "FRANCO ORTHMANN", "FRANZ EDUARD PRUNER KRAUSE",
			"GABRIEL AUGUSTO MELO", "GABRIEL BUSANNA", "GABRIEL HENRIQUE FISCHER VIEIRA", "GABRIEL LUIZ MENEGHELLI",
			"GABRIELA DA SILVA PEREIRA", "GAUTIER DE OLIVEIRA WROSNKI", "GIGRIANI NAIARA JARDIM DE OLIVEIRA",
			"GILIARDI ANTONIO MATIAS", "GIOVANA RIBEIRO SCHIAVINI", "GLÁUCIA ORTIZ GARCIA", "GLAUCIELLE ORTIZ GARCIA",
			"GRAZIELA MORAES CORREA", "GUILHERME TROCATTI", "GUILHERME WELLINGTON KREUTZ", "GUSTAVO ARNALDO FAQUETI",
			"GUSTAVO MAX BAEHR", "GUSTAVO VICTOR OLEGÁRIO", "HAMILTON RONI LENARTE", "HEITOR FERREIRA DE CARVALHO",
			"HELOIZA WOLLICK", "HENRIQUE SCHWARZROCK", "ISABEL ELIZANDRA FELISMINO DA SILVA",
			"IVAN MANOEL DA SILVA FILHO", "JANAINA FUSIGNER", "JAQUELINE KRATZ ROCCO", "JEAN CARLOS PIAIA",
			"JENNIFER CLOCK DOS SANTOS", "JENNINGS TIEDT JUNIOR", "JESSE AMADEU VINOTTI", "JÉSSICA APARECIDA RAIF",
			"JÉSSICA EBEL KISTNER", "JESSICA HARDT", "JESSICA LEAL ALVES", "JÉSSICA QUINOTT DA SILVA",
			"JÉSSICA SEIXAS DA SILVA", "JÉSSICA SILVÉRIO DE SOUZA", "JESSICA ZIMMERMANN DE JESUS",
			"JHONATAN SANTOS DA SILVA", "JOÃO ANTONIO DE OLIVEIRA", "JOAO CARLOS BRITTO", "JOAO CARLOS DOS SANTOS",
			"JOÃO PAULO MACHADO", "JOÃO VITOR DA ROCHA", "JOEL PIRES DE LIMA", "JOHNNY GABRIEL ROCHA",
			"JOILSON WOLNEY DAY", "JONATHAN ALVES DOS SANTOS", "JONATHAN DA SILVA", "JONATHAN DE SOUZA",
			"JONATHAN MONTAGNA", "JONATHAN NAGEL", "JONATHAN NOWASKY", "JOSÉ CARLOS BUSS", "JOSIANE BORGHESAN",
			"JUDITE BRUNER", "JÚLIO CESAR RAULINO", "JÚLIO CÉSAR SCHNEIDER", "JULIO CEZAR FARIAS",
			"JUMARA CRISTINA DA SILVEIRA ALVES", "JURANDI TENFEN", "KARENINE RAFAELA ARENHART",
			"KARINA DE PAULA CENE DA SILVA", "KAROLINE SCHÄFER", "KEILIANNE FARIAS DO NASCIMENTO",
			"KELVIN ADELINO HILLESHEIM", "KETSIA ABIGAIL DE LIMA SOUZA", "KIMBERLY FERREIRA VIANA",
			"LARISSA KELLY RIBEIRO CARVALHO", "LARISSA SINDY MAKUFKA SCHLICHTING", "LAUREANO VICENTI JÚNIOR",
			"LEANDRO DA SILVA", "LEANDRO DA SILVA", "LEIRIANA FRIESE DE ALMEIDA", "LÉO MATHIAS HANG",
			"LEONARDO MANOEL CLEMENTE", "LEONARDO WESLSY FRAÇA", "LETICIA CRISTIANE MULLER", "LETÍCIA INDIARA DE LIZ",
			"LETÍCIA WOELFER DE OLIVEIRA", "LILIAN DAIANA ALVES", "LILIANE PEREIRA CAMARGO", "LIVIO ZANELLA JUNIOR",
			"LUAN FERNANDES DOS SANTOS", "LUAN SOUZA PERUZZOLO", "LUANA TECH", "LUCAS ARIEL CLAUDINO",
			"LUCAS DE FARIAS", "LUCAS DE OLIVEIRA", "LUCAS EDUARDO METZGER", "LUCAS HOMERO HERKENHOFF",
			"LUCAS KARKLE COUTO", "LUCAS SANTIAGO DA COSTA", "LUCAS TEIXEIRA JÚNIOR", "LUCAS WALIM DA SILVA",
			"LUCAS ZUNINO", "LUCIANO CLÉCIO DA SILVA", "LUCIANO GONÇALVES JUNIOR", "LUCIANO REITZ", "LUCIANO SCHWORZ",
			"LÚCIO MARCOS DE ANDRADE", "LUIS EDUARDO SOUZA SOARES", "LUIS NELSON SARAIVA CALAGE", "LUIZ EDUARDO KRAUS",
			"LUIZA ESPINDULA RODRIGUES DE ALMEIDA", "LUIZA ZIMMERMANN DALTRO", "LUKAS RONÂN MENDES", "MAICON DA SILVA",
			"MAICOSAUTNER", "MAIKON MACHADO", "MARCELA CRISTINA AGOSTINHO", "MARCIA JOSEFA CEZAR",
			"MARCIO ADRIANO DA SILVA", "MARCO ANTONIO DE MATOS SOARES", "MARCO AURÉLIO VOELZ DE SOUZA",
			"MARCOS ALEXANDRE DE AMORIM", "MARCOS AURELIO WEIDGENANNT", "MARCOS DA SILVA", "MARGRID VENANCIO",
			"MARIA EDUARDA DE SOUZA BOOZ", "MARÍLHA VERGÍNIO BIAVA", "MARINA APARECIDA ROSA",
			"MARLON ANDREWS DE SOUZA", "MATEUS SCHLINGMANN LANSER", "MATEUS SIMON DAS NEVES", "MATHEUS AUGUSTO PITES",
			"MATHEUS DE MELO FERREIRA", "MATHEUS FILIPE PIRAN", "MAURICIO AGOSTINHO DA SILVA VIEIRA",
			"MAURÍCIO TONETT", "MAXIMILIANO CECHETTO REINERT", "MAYCON METZNER", "MICHEL KRAUS MENSOR",
			"MICHELI KAZIK PEREIRA", "MIGUEL ZIMMERMANN", "MURILLO SCABIO MAGALHÃES", "NAIANE CAROLINA SCHMITT",
			"NARJARA GOERTTMANN", "NEUDO SAMPIETRO", "NILSON FARIAS DE SOUZA", "ODAIR ROBSON ZUSE FERREIRA",
			"ONERES GODINHO ARAUJO", "OSMAR MATHIAS JUNIOR", "OSMAR PADILHA", "OSVALDO DE SOUSA JUNIOR",
			"OTÁVIO AUGUSTO DA SILVA SOUZA", "OTÁVIO DE FRANÇA DA SILVA", "PALOMA WETZEL DIAS", "PAMELA ROEDER VIEIRA",
			"PATRÍCIA BIANCA TEWS DE MOURA", "PATRICIA DE FATIMA RIBEIRO", "PATRICIA GONÇALVES",
			"PATRICK (PATRICK R. PRIM)", "PAULO EDUARDO SILVA", "PAULO HENRIQUE DE SOUZA SANTANA",
			"PRISCILA DA SILVA RAULINO CHAVES", "PRISCILA ELISA CORREA DA SILVA", "PRISCILA ROCHA VIEIRA",
			"PRISCILA SCHNAIDER THEISS", "PRISCILA SILVEIRA CAVALHEIRO", "RAFAEL BINSFELD", "RAFAEL FERRETTI",
			"RAFAEL LEITE", "RAFAEL NOVELLO", "RAFAEL RAMOS", "RAFAELA CRISTINA COELHO SCHNAIDER",
			"RAÍ ERNANDES DA SILVA", "RAMON FABIAN ANTUNES PFEIFER", "RICARDO LOURENÇO DOS SANTOS", "RICARDO LUCIANO",
			"RICHARD DOUGLAS DA SILVA", "ROBERTA LAÍSE BATISTA", "ROBSON DESIDÉRIO DA SILVA", "RODRIGO COTA",
			"RODRIGO DA SILVA", "RODRIGO DE OLIVEIRA", "RODRIGO FERREIRA MARTINS", "RODRIGO SOUZA FELIZARDO",
			"ROGERIO TOMACHEUSKI DE OLIVEIRA", "ROMÁRIO ALVES DA CRUZ", "RONAN KROEGER", "ROQUE RUAN DA SILVA",
			"SABRINA FRANÇA DE MORAES", "SABRINA LOBO", "SABRINA LUCIANA ZIMMERMANN", "SABRINA MIRANDA",
			"SAMOEL TEIXEIRA BRANT", "SANDRO PAULO KOTH", "SARA APARECIDA KICHOLLA FERNANDES",
			"SAULO ADRIANO STARAUCHEK", "SIDNEI JOSÉ CORRÊA JUNIOR", "SUELEN DALPIAZ", "SUELLEN ALVES FRANCISCO",
			"SUZANA IONGBLOOD", "TALITA SOARES", "TAMARA SCHIRMER", "TARSYS PAOLO CORDEIRO",
			"TATHIANE TAIS DIAS MORAES PITA", "THAINÁ FLÔR FERRETTI", "THAIS DE SOUZA MELO RODRIGUES",
			"THAISE CORDEIRO DA SILVA", "THAMYRIS NOGUEIRA LOPES", "THIAGO ANDERSON DA SILVA", "THIAGO ANDRÉ IGNACIO",
			"THIAGO GOEBEL", "THIAGO ROLOFF", "TIAGO VAGNER", "VALDIR CESAR DE AMORIM JUNIOR",
			"VANDERSON MACHADO COSTA", "VICTOR TELES DOS SANTOS", "VICTORIA GERMER ALVES", "VINÍCIUS CORREIA SAMPAIO",
			"WANDER ISRAEL LEMOS", "WELLINGTON AMANCIO BARTH", "WENDEL JONHAN VINTER", "WILLIAM FAGUNDES DOS SANTOS",
			"WILLIAM GUSTAVO MARQUETTI", "WILLIAM HADLICH", "WILLIAM RIBEIRO PROENÇA",
			"WILLIAN CARLOS ALBERTO DE OLIVEIRA", "YAGO SÁVIO DE SOUZA OLIVEIRA", "ZADIR RODRIGO GONCALVES" };

	private static File pasta;

	private static boolean vaiNoPronto = false;

	public static void main(String[] args) throws IOException {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Pasta para gerar os arquivos: ");
			pasta = new File(scanner.nextLine());
			pasta.mkdirs();

			if (!pasta.isDirectory()) {
				throw new RuntimeException("Não conseguiu criar a pasta: " + pasta.getAbsolutePath());
			}
		}

		if (vaiNoPronto) {
			gerarAlunos();
			gerarCursos();
			gerarDisciplinas();
			gerarHistorico();
			gerarMensalidades();
			gerarSolicitacoesMatricula();
			gerarSolicitacoesDiploma();
		} else {
			List<Aluno> alunos = criarAlunos();
			List<Disciplina> disciplinas = criarDisciplinas();
			List<Curso> cursos = criarCursos(disciplinas);
			List<Historico> historicos = criarHistoricos(alunos, cursos);
			List<Mensalidade> mensalidades = criarMensalidades(historicos);
			List<SolMatricula> solMatriculas = criarSolMatriculas(alunos, cursos);
			List<SolDiploma> solDiplomas = criarSolDiplomas(alunos, cursos);
			criarCasosFelizes(alunos, disciplinas, cursos, historicos, mensalidades, solMatriculas, solDiplomas);

			gravarAlunos(alunos);
			gravarDisciplinas(disciplinas);
			gravarCursos(cursos);
			gravarHistoricos(historicos);
			gravarMensalidades(mensalidades);
			gravarSolMatriculas(solMatriculas);
			gravarSolDiplomas(solDiplomas);
		}
		System.out.println("Pronto!");
	}

	private static List<Aluno> criarAlunos() {
		List<Aluno> alunos = new ArrayList<>(NOMES_ALUNOS.length);
		for (int i = 0; i < NOMES_ALUNOS.length; i++) {
			alunos.add(new Aluno(i + 1000, NOMES_ALUNOS[i], RANDOM.nextBoolean()));
		}
		return alunos;
	}

	private static List<Disciplina> criarDisciplinas() {
		List<Disciplina> disciplinas = new ArrayList<>(NOMES_DISCIPLINAS.length);
		for (int i = 0; i < NOMES_DISCIPLINAS.length; i++) {
			double valorMatricula = RANDOM.nextDouble() * 1000 + 300;
			disciplinas.add(new Disciplina(i + 1000, NOMES_DISCIPLINAS[i], valorMatricula, valorMatricula
					- RANDOM.nextDouble() * 100));
		}

		return disciplinas;
	}

	private static List<Curso> criarCursos(List<Disciplina> disciplinas) {
		List<Curso> cursos = new ArrayList<>(NOMES_CURSOS.length);
		final int totalDisciplinas = disciplinas.size();
		for (int i = 0; i < NOMES_CURSOS.length; i++) {
			int qtDisc = RANDOM.nextInt(25) + 10;
			Set<Disciplina> selectedDisciplinas = new HashSet<>(qtDisc);
			for (int d = 0; d < qtDisc; d++) {
				selectedDisciplinas.add(disciplinas.get(RANDOM.nextInt(totalDisciplinas)));
			}
			cursos.add(new Curso(i + 1000, NOMES_CURSOS[i], disciplinas));
		}
		return cursos;
	}

	private static List<Historico> criarHistoricos(List<Aluno> alunos,
			List<Curso> cursos) {
		
		Set<Historico> historicos = new HashSet<>();
		int totalAlunos = (RANDOM.nextInt(alunos.size() - 50) + 50); // de 50 até todos os alunos
		Set<Aluno> alunosSorteados = new HashSet<>();
		for (int a = 0; a < totalAlunos; a++) {
			Aluno alunoSorteado;
			do {
				alunoSorteado = alunos.get(RANDOM.nextInt(alunos.size() - 1));
			} while (!alunosSorteados.add(alunoSorteado));
			Set<Curso> cursosAluno = new HashSet<>(cursos);
			int totalCursos = RANDOM.nextInt(4) + 1; // de 1 até 5 cursos
			for (int c = 0; c < totalCursos; c++) {
				Curso curso;
				while (cursosAluno.add(curso = cursos.get(RANDOM.nextInt(cursos.size() - 1)))); // sem repetir cursos
				
				List<Disciplina> disciplinasCurso = curso.disciplinas;
				int totalDisciplinas = RANDOM.nextInt(disciplinasCurso.size() - 1) + 1; // de 1 até todas as disciplinas uma vez
				for (int d = 0; d < totalDisciplinas; d++) {
					Historico historico;
					do {
						Disciplina disciplina = disciplinasCurso.get(RANDOM.nextInt(disciplinasCurso.size() - 1));
						SituacaoDisciplina sit = SITUACOES_DISCIPLINAS[RANDOM.nextInt(SITUACOES_DISCIPLINAS.length - 1)];
						Calendar dtIni = Calendar.getInstance();
						dtIni.set(2000 + RANDOM.nextInt(13), RANDOM.nextInt(11), RANDOM.nextInt(28));
						
						historico = new Historico(alunoSorteado, disciplina, curso, dtIni, sit);
					} while (!historicos.add(historico));
				}
			}
		}
				
		return new ArrayList<>(historicos);
	}

	private static List<Mensalidade> criarMensalidades(List<Historico> historicos) {
		int qtMensalidades = RANDOM.nextInt(historicos.size() / 2) + historicos.size() / 2; // de metade a todos
		List<Mensalidade> mensalidades = new ArrayList<>(qtMensalidades);
		
		for (int mens = 0; mens < qtMensalidades; mens++) {
			Historico h = historicos.get(mens);
			Calendar competencia = Calendar.getInstance();
			competencia.set(2000 + RANDOM.nextInt(13), RANDOM.nextInt(11), RANDOM.nextInt(28));
			Calendar vecto = (Calendar) competencia.clone();
			vecto.add(Calendar.MONTH, 1);
			Mensalidade m = new Mensalidade(h.aluno, h.disciplina.valorMatricula * RANDOM.nextInt(4) + 1, competencia, vecto, RANDOM.nextBoolean());
			mensalidades.add(m);
		}
		
		
		return mensalidades;
	}

	private static List<SolMatricula> criarSolMatriculas(List<Aluno> alunos, List<Curso> cursos) {
		int qtSols = RANDOM.nextInt(alunos.size() / 2) + alunos.size() / 2; // de metade a todos
		Set<SolMatricula> solMatriculas = new HashSet<>();
		
		for (int solIndex = 0; solIndex < qtSols; solIndex++) {
			Curso curso = cursos.get(RANDOM.nextInt(cursos.size() - 1));
			List<Disciplina> disciplinas = curso.disciplinas;
			int qtDisc = RANDOM.nextInt(disciplinas.size() - 1) + 1;
			for (int d = 0; d < qtDisc; d++) {
				SolMatricula sol;
				do {
					sol = new SolMatricula(alunos.get(RANDOM.nextInt(alunos.size())), // aluno 
							disciplinas.get(RANDOM.nextInt(disciplinas.size() - 1)), // disciplina
							curso); // curso
				} while (!solMatriculas.add(sol));
			}
		}
		
		return new ArrayList<>(solMatriculas);
	}

	private static List<SolDiploma> criarSolDiplomas(List<Aluno> alunos, List<Curso> cursos) {
		int qtSols = RANDOM.nextInt(alunos.size() - 1) + 1;
		Set<SolDiploma> solDiplomas = new HashSet<>(qtSols);
		for (int solIndex = 0; solIndex < qtSols; solIndex++) {
			SolDiploma sol;
			do {
				sol = new SolDiploma(alunos.get(RANDOM.nextInt(alunos.size() - 1)), // aluno 
						cursos.get(RANDOM.nextInt(cursos.size() - 1))); // curso
			} while (!solDiplomas.add(sol));
		}
		return new ArrayList<>(solDiplomas);
	}

	private static void criarCasosFelizes(List<Aluno> alunos, List<Disciplina> disciplinas, List<Curso> cursos,
			List<Historico> historicos, List<Mensalidade> mensalidades, List<SolMatricula> solMatriculas,
			List<SolDiploma> solDiplomas) {
		
		Aluno aluno = new Aluno(2000, "Aluno Bom", true);
		for (Curso curso : cursos) {
			for (Disciplina disciplina : curso.disciplinas) {
				historicos.add(new Historico(aluno, disciplina, curso, Calendar.getInstance(), SituacaoDisciplina.APROVADO));
			}
		}
		mensalidades.add(new Mensalidade(aluno, 100, Calendar.getInstance(), Calendar.getInstance(), true));
		alunos.add(aluno);
	}

	private static void gravarAlunos(List<Aluno> alunos) throws IOException {
		File file = new File(pasta, AlunosReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < alunos.size(); i++) {
			Aluno a = alunos.get(i);
			writeReg(ps, a.cod, a.nome, a.ativo); 
			if (i < alunos.size() - 1) {
				ps.println();
			}
		}

	}

	private static void gravarDisciplinas(List<Disciplina> disciplinas) throws IOException {
		File file = new File(pasta, DisciplinasReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < disciplinas.size(); i++) {
			Disciplina d = disciplinas.get(i);
			writeReg(ps, d.cod, // codDisciplina
					d.nome, // nome
					d.valorMatricula, // valorMatric
					d.valorMensal); // valorMensal
			if (i < disciplinas.size() - 1) {
				ps.println();
			}
		}
	}

	private static void gravarCursos(List<Curso> cursos) throws IOException {
		File file = new File(pasta, CursoReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		StringBuilder sb;
		for (int i = 0; i < cursos.size(); i++) {
			Curso curso = cursos.get(i);
			List<Disciplina> disciplinas = curso.disciplinas;
			sb = new StringBuilder();
			for (int d = 0; d < disciplinas.size(); d++) {
				sb.append(disciplinas.get(d).cod);
				if (d < disciplinas.size() - 1) {
					sb.append(';');
				} 
			}
			
			writeReg(ps, curso.cod, // cod
					curso.nome, // nome
					sb.toString()); // listaDisciplinas
			if (i < cursos.size() - 1) {
				ps.println();
			}
		}
	}

	private static void gravarHistoricos(List<Historico> historicos) throws IOException {
		File file = new File(pasta, HistoricosReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < historicos.size(); i++) {
			Historico historico = historicos.get(i);
			writeReg(ps, historico.aluno.cod, // codAluno
					historico.disciplina.cod, // codDisciplina
					historico.curso.cod, // codCurso
					calendarToString(historico.dataInicio), // dataInicio
					historico.situacao.toString()); // situacao
			if (i < historicos.size() - 1) {
				ps.println();
			}
		}
	}

	private static void gravarMensalidades(List<Mensalidade> mensalidades) throws IOException {
		File file = new File(pasta, MensalidadesReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < mensalidades.size(); i++) {
			Mensalidade mensalidade = mensalidades.get(i);
			writeReg(ps, mensalidade.aluno.cod, // codAluno
					mensalidade.valor, // valor
					calendarToString(mensalidade.competencia), // competencia
					calendarToString(mensalidade.vcto), // vencimento
					mensalidade.isPaga); // isPaga
			if (i < mensalidades.size() - 1) {
				ps.println();
			}
		}
	}
	
	private static String calendarToString(Calendar c) {
		String format = "%1$td/%1$tm/%1$tY";
//		return c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR); 
		return String.format(format, c);
	}

	private static void gravarSolMatriculas(List<SolMatricula> solMatriculas) throws IOException {
		File file = new File(pasta, SolicitacoesMatriculaReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < solMatriculas.size(); i++) {
			SolMatricula sol = solMatriculas.get(i);
			writeReg(ps, sol.aluno.cod, // codAluno
					sol.disciplina.cod, // codDisciplina
					sol.curso.cod); // codCurso
			if (i < solMatriculas.size() - 1) {
				ps.println();
			}
		}
	}

	private static void gravarSolDiplomas(List<SolDiploma> solDiplomas) throws IOException {
		File file = new File(pasta, SolicitacoesDiplomaReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < solDiplomas.size(); i++) {
			SolDiploma sol = solDiplomas.get(i);
			writeReg(ps, sol.aluno.cod, // codAluno
					sol.curso.cod); // codCurso
			if (i < solDiplomas.size() - 1) {
				ps.println();
			}
		}
	}
	
	// <<<<<----- INÍCIO ANTIGO -----

	private static void gerarAlunos() throws IOException {
		File file = new File(pasta, AlunosReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < NOMES_ALUNOS.length; i++) {
			writeReg(ps, i + 1000, // codAluno
					NOMES_ALUNOS[i], // nome
					RANDOM.nextInt(1) != 0); // ativo
			if (i < NOMES_ALUNOS.length - 1) {
				ps.println();
			}
		}

	}

	private static void gerarCursos() throws IOException {
		File file = new File(pasta, CursoReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		StringBuilder sb;
		for (int i = 0; i < NOMES_CURSOS.length; i++) {
			sb = new StringBuilder();
			randomDisciplinasCodes(sb);
			writeReg(ps, i + 1000, // cod
					NOMES_CURSOS[i], // nome
					sb.toString()); // listaDisciplinas
			if (i < NOMES_CURSOS.length - 1) {
				ps.println();
			}
		}
	}

	private static void randomDisciplinasCodes(StringBuilder sb) {
		int qt = RANDOM.nextInt(20) + 5;
		for (int i = 0; i < qt; i++) {
			sb.append(RANDOM.nextInt(NOMES_DISCIPLINAS.length) + 100);
			if (i < qt - 1) {
				sb.append(';');
			}
		}

	}

	private static void gerarDisciplinas() throws IOException {
		File file = new File(pasta, DisciplinasReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		for (int i = 0; i < NOMES_DISCIPLINAS.length; i++) {
			writeReg(ps, i + 100, // codDisciplina
					NOMES_DISCIPLINAS[i], // nome
					RANDOM.nextDouble() + 30.0, // valorMatric
					RANDOM.nextDouble() + 25.0); // valorMensal
			if (i < NOMES_DISCIPLINAS.length - 1) {
				ps.println();
			}
		}
	}

	private static void gerarHistorico() throws IOException {
		File file = new File(pasta, HistoricosReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		int qtHistorico = NOMES_ALUNOS.length * 5; // magic number, yeah
		for (int i = 0; i < qtHistorico; i++) {
			writeReg(ps, 1000 + RANDOM.nextInt(NOMES_ALUNOS.length), // codAluno
					100 + RANDOM.nextInt(NOMES_DISCIPLINAS.length), // codDisciplina
					1000 + RANDOM.nextInt(NOMES_DISCIPLINAS.length), // codCurso
					"02/03/2014", // dataInicio
					SITUACOES_DISCIPLINAS[RANDOM.nextInt(SITUACOES_DISCIPLINAS.length)]); // situacao
			if (i < qtHistorico - 1) {
				ps.println();
			}
		}
	}

	private static void gerarMensalidades() throws IOException {
		File file = new File(pasta, MensalidadesReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		int qtMensalidades = NOMES_ALUNOS.length * 5; // 5 meses, pq Steve Jobs
														// quis
		for (int i = 0; i < qtMensalidades; i++) {
			writeReg(ps, 1000 + RANDOM.nextInt(NOMES_ALUNOS.length), // codAluno
					(double) (25 + RANDOM.nextInt(300)), // valor
					"03/04/2014", // competencia
					"02/05/2014", // vencimento
					RANDOM.nextInt(1) != 0); // isPaga
			if (i < qtMensalidades - 1) {
				ps.println();
			}
		}
	}

	private static void gerarSolicitacoesMatricula() throws IOException {
		File file = new File(pasta, SolicitacoesMatriculaReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		int qtSolics = NOMES_ALUNOS.length * 4; // pq Steve Jobs quis
		for (int i = 0; i < qtSolics; i++) {
			writeReg(ps, 1000 + RANDOM.nextInt(NOMES_ALUNOS.length), // codAluno
					100 + RANDOM.nextInt(NOMES_DISCIPLINAS.length), // codDisciplina
					1000 + RANDOM.nextInt(NOMES_CURSOS.length)); // codCurso
			if (i < qtSolics - 1) {
				ps.println();
			}
		}
	}

	private static void gerarSolicitacoesDiploma() throws IOException {
		File file = new File(pasta, SolicitacoesDiplomaReader.FILE_NAME);
		file.createNewFile();
		PrintStream ps = new PrintStream(file);
		int qtSolics = NOMES_ALUNOS.length * 4; // pq Steve Jobs quis
		for (int i = 0; i < qtSolics; i++) {
			writeReg(ps, 1000 + RANDOM.nextInt(NOMES_ALUNOS.length), // codAluno
					1000 + RANDOM.nextInt(NOMES_CURSOS.length)); // codCurso
			if (i < qtSolics - 1) {
				ps.println();
			}
		}
	}
	
	// ----- FIM ANTIGO ----->>>>>

	private static final void writeReg(PrintStream ps, Object... values) {
		for (int i = 0; i < values.length; i++) {
			ps.print(String.valueOf(values[i]));
			if (i < values.length - 1) {
				ps.print(',');
			}
		}
	}

	private static class Aluno {

		int cod;
		String nome;
		boolean ativo;

		public Aluno(int cod, String nome, boolean ativo) {
			this.cod = cod;
			this.nome = nome;
			this.ativo = ativo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (ativo ? 1231 : 1237);
			result = prime * result + cod;
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Aluno other = (Aluno) obj;
			if (ativo != other.ativo)
				return false;
			if (cod != other.cod)
				return false;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			return true;
		}

	}

	private static class Mensalidade {

		Aluno aluno;
		double valor;
		Calendar competencia;
		Calendar vcto;
		boolean isPaga;

		public Mensalidade(Aluno aluno, double valor, Calendar competencia, Calendar vcto, boolean isPaga) {
			this.aluno = aluno;
			this.valor = valor;
			this.competencia = competencia;
			this.vcto = vcto;
			this.isPaga = isPaga;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
			result = prime * result + ((competencia == null) ? 0 : competencia.hashCode());
			result = prime * result + (isPaga ? 1231 : 1237);
			long temp;
			temp = Double.doubleToLongBits(valor);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((vcto == null) ? 0 : vcto.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Mensalidade other = (Mensalidade) obj;
			if (aluno == null) {
				if (other.aluno != null)
					return false;
			} else if (!aluno.equals(other.aluno))
				return false;
			if (competencia == null) {
				if (other.competencia != null)
					return false;
			} else if (!competencia.equals(other.competencia))
				return false;
			if (isPaga != other.isPaga)
				return false;
			if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
				return false;
			if (vcto == null) {
				if (other.vcto != null)
					return false;
			} else if (!vcto.equals(other.vcto))
				return false;
			return true;
		}

	}

	private static class Disciplina {

		int cod;
		String nome;
		double valorMatricula;
		double valorMensal;

		public Disciplina(int cod, String nome, double valorMatricula, double valorMensal) {
			this.cod = cod;
			this.nome = nome;
			this.valorMatricula = valorMatricula;
			this.valorMensal = valorMensal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cod;
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			long temp;
			temp = Double.doubleToLongBits(valorMatricula);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(valorMensal);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Disciplina other = (Disciplina) obj;
			if (cod != other.cod)
				return false;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			if (Double.doubleToLongBits(valorMatricula) != Double.doubleToLongBits(other.valorMatricula))
				return false;
			if (Double.doubleToLongBits(valorMensal) != Double.doubleToLongBits(other.valorMensal))
				return false;
			return true;
		}

	}

	private static class Curso {

		int cod;
		String nome;
		List<Disciplina> disciplinas;

		public Curso(int cod, String nome, List<Disciplina> disciplinas) {
			this.cod = cod;
			this.nome = nome;
			this.disciplinas = disciplinas;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cod;
			result = prime * result + ((disciplinas == null) ? 0 : disciplinas.hashCode());
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Curso other = (Curso) obj;
			if (cod != other.cod)
				return false;
			if (disciplinas == null) {
				if (other.disciplinas != null)
					return false;
			} else if (!disciplinas.equals(other.disciplinas))
				return false;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			return true;
		}

	}

	private static class Historico {

		Aluno aluno;
		Disciplina disciplina;
		Curso curso;
		Calendar dataInicio;
		SituacaoDisciplina situacao;

		public Historico(Aluno aluno, Disciplina disciplina, Curso curso, Calendar dataInicio,
				SituacaoDisciplina situacao) {
			this.aluno = aluno;
			this.disciplina = disciplina;
			this.curso = curso;
			this.dataInicio = dataInicio;
			this.situacao = situacao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
			result = prime * result + ((curso == null) ? 0 : curso.hashCode());
			result = prime * result + ((dataInicio == null) ? 0 : dataInicio.hashCode());
			result = prime * result + ((disciplina == null) ? 0 : disciplina.hashCode());
			result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Historico other = (Historico) obj;
			if (aluno == null) {
				if (other.aluno != null)
					return false;
			} else if (!aluno.equals(other.aluno))
				return false;
			if (curso == null) {
				if (other.curso != null)
					return false;
			} else if (!curso.equals(other.curso))
				return false;
			if (dataInicio == null) {
				if (other.dataInicio != null)
					return false;
			} else if (!dataInicio.equals(other.dataInicio))
				return false;
			if (disciplina == null) {
				if (other.disciplina != null)
					return false;
			} else if (!disciplina.equals(other.disciplina))
				return false;
			if (situacao != other.situacao)
				return false;
			return true;
		}

	}

	private static class SolMatricula {

		Aluno aluno;
		Disciplina disciplina;
		Curso curso;

		public SolMatricula(Aluno aluno, Disciplina disciplina, Curso curso) {
			this.aluno = aluno;
			this.disciplina = disciplina;
			this.curso = curso;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
			result = prime * result + ((curso == null) ? 0 : curso.hashCode());
			result = prime * result + ((disciplina == null) ? 0 : disciplina.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SolMatricula other = (SolMatricula) obj;
			if (aluno == null) {
				if (other.aluno != null)
					return false;
			} else if (!aluno.equals(other.aluno))
				return false;
			if (curso == null) {
				if (other.curso != null)
					return false;
			} else if (!curso.equals(other.curso))
				return false;
			if (disciplina == null) {
				if (other.disciplina != null)
					return false;
			} else if (!disciplina.equals(other.disciplina))
				return false;
			return true;
		}

	}

	private static class SolDiploma {

		Aluno aluno;
		Curso curso;

		public SolDiploma(Aluno aluno, Curso curso) {
			this.aluno = aluno;
			this.curso = curso;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
			result = prime * result + ((curso == null) ? 0 : curso.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SolDiploma other = (SolDiploma) obj;
			if (aluno == null) {
				if (other.aluno != null)
					return false;
			} else if (!aluno.equals(other.aluno))
				return false;
			if (curso == null) {
				if (other.curso != null)
					return false;
			} else if (!curso.equals(other.curso))
				return false;
			return true;
		}

	}

}
