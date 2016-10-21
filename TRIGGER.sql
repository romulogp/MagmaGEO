--TRIGGER QUE PERCORRE BAIRROS PERGUNTANDO SE UM PONTO FAZ PARTE DA GEOMETRIA DO BAIRRO
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


INSERT INTO ocorrencia (descricao, id_tipo, geometria) VALUES (
	'Acho que eu vi um gatinho.', 
	1, 
	st_geomfromtext('POINT(-48.660922 -26.914005)', 4326)
);


INSERT INTO ocorrencia (id, descricao, id_tipo, geometria) VALUES (
	50,
	'OCORRENCIA50',
	50, 
	st_geomfromtext('POINT(-48.87884391536012 -27.20762056488141)', 4326)
);

SELECT * FROM ocorrencia

DELETE FROM ocorrencia


SELECT b.nome, st_astext(b.geometria), st_astext(o.geometria) FROM ocorrencia o INNER JOIN bairro b
	ON (o.id_bairro = b.id)
	WHERE
	ST_Contains(b.geometria, o.geometria);

/** RETURN BOOLEAN **/
SELECT ST_Contains(
	st_geomfromtext('POLYGON((
		-48.32207435176101 -26.35582633528281,
		-49.2110994259642 -26.05328651573816, 
		-50.38862735714671 -26.77764369514184, 
		-50.4653992002867 -27.7746801099752, 
		-49.24528146957998 -28.41282426494273, 
		-47.68706293301484 -27.89619619751072,
		-48.32207435176101 -26.35582633528281))', 4326),
	st_geomfromtext('POINT(-48.660922 -26.914005)', 4326)
);