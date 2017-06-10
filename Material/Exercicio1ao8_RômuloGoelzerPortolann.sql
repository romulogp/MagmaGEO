/* EXERCÍCIO 1
	Crie uma função que some dois números inteiros 
	recebidos por parâmetro, retorne o valor da soma
	e armazene a operação na tabela “somalog”
*/   
CREATE OR REPLACE FUNCTION soma(int, int) RETURNS integer AS $$
	INSERT INTO somalog(valor1, valor2, resultado) VALUES ($1, $2, $1+$2);
	SELECT $1+$2;
$$
LANGUAGE SQL;

select soma(4,5);




/* EXERCÍCIO 2
	Crie uma função que retorne a quantidade
	(tipo bigint) de alunos com média maior ou
	igual a 6 na tabela “aluno”
*/   
CREATE OR REPLACE FUNCTION qtdAprovados() RETURNS bigint AS $$
	SELECT COUNT(*) FROM aluno WHERE media >= 6;
$$
LANGUAGE SQL;

select qtdAprovados();




/* EXERCÍCIO 3
	Crie uma função que retorne os alunos com
	média maior ou igual a 6 na tabela “aluno”
*/   
CREATE OR REPLACE FUNCTION aprovados() RETURNS SETOF aluno AS $$
	SELECT * FROM aluno WHERE media >= 6;
$$
LANGUAGE SQL;

select aprovados();




/* EXERCÍCIO 4 
	Crie uma função que receba dois números
	(float) e o sinal da operação (char) por
	parâmetro, retorne o valor da operação e
	armazene a operação na tabela
	“calculadoralog”
*/
CREATE OR REPLACE FUNCTION calcula (float, float, char) RETURNS float AS $$
DECLARE
	result float;
BEGIN
	IF $3 = '+' THEN
		result = $1 + $2;
	ELSIF $3 = '-' THEN
		result = $1 - $2;
	ELSIF $3 = '/' THEN
		IF $2 != 0 THEN
			result = $1 / $2;
		ELSE
			raise exception 'Não é possível dividir por 0';
		END IF;
	ELSIF $3 = '*' THEN
		result = $1 * $2;
	ELSE
		raise exception 'Operação inválida';
	END IF;

	INSERT INTO calculadoralog(valor1, valor2, operacao, resultado) 
		VALUES ($1, $2, $3, result);

	RETURN result;
END;
$$ LANGUAGE plpgsql;

select calcula(4,2,'/');




/* EXERCÍCIO 5
	Crie uma função que receba o grau da direção
	do vendo e retorne a descrição da direção
	(Norte, Sul, Leste ou Oeste)
*/
CREATE OR REPLACE FUNCTION direcaoVento(float) RETURNS character varying AS $$
BEGIN
	IF (($1 > 315 AND $1 < 360) OR ($1 >= 0 AND $1 <= 45)) THEN
		RETURN 'NORTE';
	ELSIF ($1 <= 135) THEN
		RETURN 'LESTE';
	ELSIF ($1 <= 225) THEN
		RETURN 'SUL';
	ELSIF ($1 <= 315) THEN
		RETURN 'OESTE';
	ELSE
		raise exception 'Direção Inválida';
	END IF;

END;
$$ LANGUAGE plpgsql;

select direcaoVento(360);




/* EXERCÍCIO 6
	Crie uma função que retorne o percentual de
	incidência (percentual) para cada direção
	(Norte, Sul, Leste ou Oeste)
*/
CREATE TYPE percentual_direcao_vento as (direcao text, percentual float);

CREATE OR REPLACE FUNCTION percentualDirecao() RETURNS setof percentual_direcao_vento AS $$
DECLARE
	aux      record;
	dirAtual text;
	accNorte integer := 0;
	accLeste integer := 0;
	accSul 	 integer := 0;
	accOeste integer := 0;
	retorno percentual_direcao_vento;
BEGIN
	FOR aux IN SELECT direcaoVento(direcao) FROM vento LOOP
		IF (aux.direcaoVento = 'NORTE') THEN
			accNorte := accNorte + 1;
		ELSIF (aux.direcaoVento = 'LESTE') THEN
			accLeste := accLeste + 1;
		ELSIF (aux.direcaoVento = 'SUL') THEN
			accSul = accSul + 1;
		ELSIF (aux.direcaoVento = 'OESTE') THEN
			accOeste = accOeste + 1;
		END IF;
	END LOOP;
		retorno.direcao = 'NORTE';
		retorno.percentual = (accNorte::float)/(accNorte::float + accLeste::float + accSul::float + accOeste::float) * 100;
		return next retorno;
		
		retorno.direcao = 'SUL';
		retorno.percentual = (accSul::float)/(accNorte::float + accLeste::float + accSul::float + accOeste::float) * 100;
		return next retorno;

		retorno.direcao = 'LESTE';
		retorno.percentual = (accLeste::float)/(accNorte::float + accLeste::float + accSul::float + accOeste::float) * 100;
		return next retorno;

		retorno.direcao = 'OESTE';
		retorno.percentual = (accOeste::float)/(accNorte::float + accLeste::float + accSul::float + accOeste::float) * 100;
		return next retorno;
END;
$$ LANGUAGE plpgsql;

select direcao, percentual from percentualDirecao();




/* Exercício 7 e 8
	Premissas:
	- Expediente: 08:00 até as 12:00, 13:30 até as 17:30
	- Não há expediente no sábado e no domingo
	- Não é permitido que um agendamento ultrapasse o
	horário do expediente (ex.: início 11:50 até 12:10)
	- Não é permitido sobreposição de horário entre agendamento

Exercício 7
	Crie uma função que receba os seguintes parâmetros:
	- timestamp: com a data de início do agendamento
	- integer: com a duração (em minutos do agendamento)
	- varchar(50): com o nome do cliente
	A função deve, se possível, inserir um agendamento
	para o cliente e retornar:
	- True: quando for possível
	- False: quando não for possível
*/
/* Realiza ou não um agendamento, conforme as premissas.
	@param
	$1 horario
	$2 tempo em minutos
	$3 nome do cliente				*/
CREATE OR REPLACE FUNCTION realizaAgendamento(timestamp, integer, varchar(50)) RETURNS boolean AS $$
DECLARE
	horarioAgendado timestamp := $1;
	duracao	        integer	  := $2;
	horarioTermino  timestamp := tempoPrevistoTermino(horarioAgendado, duracao);
	diaDaSemana     integer	  := extract(DOW FROM $1::timestamp);
BEGIN
	IF (agendaLivreEntre(horarioAgendado, horarioTermino)) THEN
		IF (ehDiaDeExpediente(horarioAgendado) AND ehHorarioDeExpediente(horarioAgendado) AND ehHorarioDeExpediente(horarioTermino)) THEN
			IF (ehIntervaloValido(horarioAgendado, horarioTermino)) THEN
				INSERT INTO agendamento(inicio, termino, cliente) VALUES (horarioAgendado, horarioTermino, $3);
				return true;
			END IF;
		END IF;
	END IF;
	RETURN false;
END;
$$ LANGUAGE plpgsql;
--select realizaAgendamento('17/08/2015 13:30:00', 60, 'Romulo');
CREATE OR REPLACE FUNCTION ehIntervaloValido(timestamp, timestamp) RETURNS boolean AS $$
DECLARE
	intervalo1_inicio  time := '12:00:00';
	intervalo1_termino time := '13:30:00';

	diaInicial  interval := extract(day    FROM $1::timestamp)|| ' day';
	horaInicial interval := extract(hour   FROM $1::timestamp)|| ' hour';
	minInicial  interval := extract(minute FROM $1::timestamp)|| ' minutes';
	horInicial  time := '00:00:00' + horaInicial + minInicial;

	diaTermino  interval := extract(day    FROM $2::timestamp)|| ' day';
	horaTermino interval := extract(hour   FROM $2::timestamp)|| ' hour';
	minTermino  interval := extract(minute FROM $2::timestamp)|| ' minutes';
	horTermino  time := '00:00:00' + horaTermino + minTermino;
BEGIN
	IF ((intervalo1_inicio >= horInicial AND intervalo1_termino <= horTermino) OR (diaInicial != diaTermino)) THEN
		RETURN false;
	END IF;
	RETURN true;
END;
$$ LANGUAGE plpgsql;
--select ehIntervaloValido('17/08/2015 11:30', '17/08/2015 12:10');
CREATE OR REPLACE FUNCTION agendaLivreEntre(timestamp, timestamp) RETURNS boolean AS $$
DECLARE
	qtdRegistros integer := count(*) FROM agendamento WHERE ((inicio >= $1 AND inicio <= $2) OR (termino >= $1 AND termino <= $2));
BEGIN
	IF ($2 > $1 AND qtdRegistros = 0) THEN
		RETURN true;
	END IF;
	RETURN false;
END;
$$ LANGUAGE plpgsql;
SELECT count(*) FROM agendamento WHERE (('18/08/2015 15:31:00' >= inicio AND '18/08/2015 15:31:00' < termino) OR ('18/08/2015 16:01:00' > termino AND '18/08/2015 16:01:00' <= termino))
--select agendaLivreEntre('18/08/2015 15:32:00', '18/08/2015 16:02:00');
CREATE OR REPLACE FUNCTION ehDiaDeExpediente(timestamp) RETURNS boolean AS $$
DECLARE
	--{{SEG=1},{TER=2},{QUA=3},{QUI=4},{SEX=5}
	diasDeExpediente integer[] := array[1,2,3,4,5];
	diaDaSemana integer := extract(DOW FROM $1::timestamp);
	--tamDiaExped integer := SELECT array_length(diasDeExpediente);
	i integer;
BEGIN
	FOREACH i IN ARRAY diasDeExpediente
	LOOP
		IF (diaDaSemana = i) THEN
			RETURN true;
		END IF;
	END LOOP;
	RETURN false;
END;
$$ LANGUAGE plpgsql;
--select ehDiaDeExpediente('14/08/2015 13:30:00');
CREATE OR REPLACE FUNCTION ehHorarioDeExpediente(timestamp) RETURNS boolean AS $$
DECLARE
	manhaInicio  time := '08:00:00';
	manhaTermino time := '12:00:00';
	tardeInicio  time := '13:30:00';
	tardeTermino time := '17:30:00';
	
	hora     interval := extract(hour   FROM $1::timestamp)|| ' hour';
	min      interval := extract(minute FROM $1::timestamp)|| ' minutes';
	horario  time := '00:00:00' + hora + min;
BEGIN
	IF ((horario >= manhaInicio AND horario <= manhaTermino) OR (horario >= tardeInicio AND horario <= tardeTermino)) THEN
		return true;
	END IF;
	return false;
END;
$$ LANGUAGE plpgsql;
--select ehHorarioDeExpediente('17/08/2015 12:00:00');					  */
CREATE OR REPLACE FUNCTION tempoPrevistoTermino(timestamp, integer) RETURNS text AS $$
DECLARE
	intervalo interval := $2|| ' minutes';
BEGIN
	return $1 + intervalo;
END; 
$$ LANGUAGE plpgsql;
--select tempoPrevistoTermino('16/08/2015 11:30', 30);

/* Exercício 8
	Crie uma função que receba o seguinte parâmetro:
	-integer com a duração (em minutos do agendamento)

	A função deve retornar a próxima data e horário (tipo timestamp)
	que há disponibilidade de agendamento (maior que a data atual)
	de acordo com a duração informada por parâmetro
*/
CREATE OR REPLACE FUNCTION buscaHorarioDisponivel(integer) RETURNS timestamp AS $$
DECLARE
	horarioInicio 	timestamp := to_char(now()::timestamp, 'DD/MM/YYYY HH24:MI');
	duracaoAgenda   integer	  := $1;
	horarioTermino 	timestamp;

	umMinuto  	interval  := '00:01:00';
	umDia		interval  := '1 day';
	
	horaAtual 	interval;
	minAtual  	interval;
	horarioAtual	time;
BEGIN
	LOOP
		horaAtual    := extract(hour   FROM horarioInicio::timestamp)|| ' hour';
		minAtual     := extract(minute FROM horarioInicio::timestamp)|| ' minutes';
		horarioAtual := '00:00:00' + horaAtual + minAtual;

		IF (horarioAtual >= '12:00:00' AND horarioAtual < '13:30:00') THEN
			horarioInicio := (extract(day FROM horarioInicio)||'/'||extract(month FROM horarioInicio)||'/'||extract(year FROM horarioInicio)||' 13:30:00')::timestamp;
		ELSIF (horarioAtual >= '17:30:00') THEN
			horarioInicio := (extract(day FROM horarioInicio)||'/'||extract(month FROM horarioInicio)||'/'||extract(year FROM horarioInicio)||' 08:00:00')::timestamp;
			horarioInicio := horarioInicio + cast('1 day' as interval);
		END IF;
		
		horarioTermino := tempoPrevistoTermino(horarioInicio, duracaoAgenda);
		
		IF (agendaLivreEntre(horarioInicio, horarioTermino)) THEN
			IF (ehDiaDeExpediente(horarioInicio) AND ehHorarioDeExpediente(horarioInicio) AND ehHorarioDeExpediente(horarioTermino)) THEN
				IF (ehIntervaloValido(horarioInicio, horarioTermino)) THEN
					RETURN horarioInicio;
				END IF;
			END IF;
		ELSIF (ehDiaDeExpediente(horarioInicio)) THEN
			horarioInicio := horarioInicio + umMinuto;
		ELSE
			horarioInicio := (extract(day FROM horarioInicio)||'/'||extract(month FROM horarioInicio)||'/'||extract(year FROM horarioInicio)||' 08:00:00')::timestamp;
			horarioInicio := horarioInicio + cast('1 day' as interval);
		END IF;
	END LOOP;
END;
$$ LANGUAGE plpgsql;
--select buscaHorarioDisponivel(30);



/*

CREATE OR REPLACE FUNCTION now_without_time_zone() RETURNS timestamp AS $$
DECLARE
	ts timestamp without time zone := to_char(now()::timestamp, 'DD/MM/YYYY HH24:MI:SS');
BEGIN
	return ts;
END;
$$ LANGUAGE plpgsql;
--select now_without_time_zone();
--select now_without_time_zone() + cast('1 day' as interval) * numberOfDays
*/