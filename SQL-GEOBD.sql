--BAIXAR EXTENSÃO ATRAVÉS DO STACKBUILDER

--USER
CREATE USER user_magmageo WITH PASSWORD 'magmageo';

--DATABASE
CREATE DATABASE db_magmageo WITH OWNER user_magmageo;

--CRIAR EXTENSÃO DO POSTGIS
CREATE EXTENSION postgis;


CREATE TABLE bairro
(
	id serial  NOT NULL,
	nome 	   character varying(50),
	CONSTRAINT pk_bairro PRIMARY KEY (id)
);
SELECT AddGeometryColumn('public', 'bairro', 'geometria', 4326, 'POLYGON', 2); --Dimensão (2 para (X, Y) e 3 para (X, Y, Z))


CREATE TABLE tipo
(
	id serial  NOT NULL,
	descricao  character varying(50),
	CONSTRAINT pk_tipo PRIMARY KEY (id)
);


CREATE TABLE ocorrencia
(
	id serial NOT NULL,
	descricao character varying(50),
	id_bairro integer,
	id_tipo   integer,
	CONSTRAINT pk_ocorrencia PRIMARY KEY (id)
);
SELECT AddGeometryColumn('public', 'ocorrencia', 'geometria', 4326, 'POINT', 2); --Dimensão (2 para (X, Y) e 3 para (X, Y, Z))

ALTER TABLE ocorrencia ADD FOREIGN KEY (id_bairro) REFERENCES bairro(id);
ALTER TABLE ocorrencia ADD FOREIGN KEY (id_tipo) REFERENCES tipo(id);

--TRIGGERS
CREATE OR REPLACE FUNCTION atualiza_bairro_da_ocorrencia() RETURNS trigger AS $$
DECLARE
	aux record;
BEGIN
	FOR aux IN SELECT id, geometria FROM bairro LOOP
		IF (ST_Contains(aux.geometria, NEW.geometria)) THEN
			NEW.id_bairro := aux.id;
		END IF;	
	END LOOP;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_bairro_da_ocorrencia BEFORE INSERT OR UPDATE ON ocorrencia
FOR EACH ROW EXECUTE PROCEDURE atualiza_bairro_da_ocorrencia();

--FUNÇÕES
CREATE TYPE kml_bairro_porcent AS (kml text, bairro text, percentual float);
CREATE OR REPLACE FUNCTION funcao_kml_bairro_porcent() RETURNS setof kml_bairro_porcent AS $$
DECLARE
	retorno kml_bairro_porcent;
	tmp perc_ocorrencia_bairro;
	auxKML text;
BEGIN
	SELECT INTO auxKML ST_asKML(bairro.geometria, 4326) FROM bairro;
	SELECT INTO tmp bairro, percentual FROM percentual_ocorrencias_por_bairro();
	retorno.kml 	   := auxKML;
	retorno.bairro     := tmp.bairro;
	retorno.percentual := tmp.percentual;
	RETURN NEXT retorno;
END;
$$ LANGUAGE plpgsql;

CREATE TYPE perc_ocorrencia_bairro AS (bairro text, percentual float);
CREATE OR REPLACE FUNCTION percentual_ocorrencias_por_bairro() RETURNS setof perc_ocorrencia_bairro AS $$
DECLARE
	aux record;
	tot_ocorr integer;
	tot_ocorr_bairro integer;
	retorno perc_ocorrencia_bairro;
BEGIN
	SELECT INTO tot_ocorr count(*) FROM ocorrencia;
	FOR aux IN SELECT id, nome FROM bairro LOOP
		SELECT INTO tot_ocorr_bairro count(*) FROM ocorrencia o INNER JOIN bairro b ON(o.id_bairro = b.id) WHERE o.id_bairro = aux.id;
		retorno.bairro     = aux.nome;
		retorno.percentual =  ((tot_ocorr_bairro::float)/(tot_ocorr::float)*100);
		RETURN NEXT retorno;
	END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT bairro, percentual FROM percentual_ocorrencias_por_bairro() ORDER BY percentual DESC;

--INSERTS
INSERT INTO bairro(nome, geometria) VALUES (
	'Fazenda',
	st_geomfromtext('POLYGON(
		( -48.67336965778321 -26.90843178027851,
		  -48.67289553630579 -26.91230393393891,
		  -48.6683890369257 -26.91219456331528,
		  -48.6694653721249 -26.90900507227601,
		  -48.67336965778321 -26.90843178027851))',
		4326)
);
INSERT INTO tipo(descricao) VALUES (
	'Local perigoso'
);
INSERT INTO ocorrencia (descricao, id_tipo, geometria) VALUES (
	'Acho que eu vi um gatinho.', 
	1, 
	st_geomfromtext('POINT(-48.660922 -26.914005)', 4326)
);

	
INSERT INTO bairro (id, nome, geometria) VALUES (
	50,
	'BAIRRO50',
	st_geomfromtext('POLYGON((
		-48.32207435176101 -26.35582633528281,
		-49.2110994259642 -26.05328651573816, 
		-50.38862735714671 -26.77764369514184, 
		-50.4653992002867 -27.7746801099752, 
		-49.24528146957998 -28.41282426494273, 
		-47.68706293301484 -27.89619619751072,
		-48.32207435176101 -26.35582633528281))',
	4326)
);
INSERT INTO tipo (id, descricao) VALUES (
	50,
	'TIPO50'
);
INSERT INTO ocorrencia (id, descricao, id_tipo, geometria) VALUES (
	50,
	'OCORRENCIA50',
	50, 
	st_geomfromtext('POINT(-48.87884391536012 -27.20762056488141)', 4326)
);