/* Exercício 1 */
CREATE OR REPLACE FUNCTION atualiza_vento() RETURNS trigger AS $$
BEGIN
	UPDATE estacao SET direcao_graus = NEW.direcao, velocidade = NEW.velocidade 
		WHERE id = NEW.id;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_vento AFTER INSERT OR UPDATE ON vento
FOR EACH ROW EXECUTE PROCEDURE atualiza_vento();

CREATE OR REPLACE FUNCTION atualiza_vento() RETURNS trigger AS $$
BEGIN
	raise notice 'atualiza_vento';
	UPDATE estacao SET direcao_graus = NEW.direcao, velocidade = NEW.velocidade 
		WHERE id = NEW.id_estacao;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_vento AFTER INSERT OR UPDATE ON vento
FOR EACH ROW EXECUTE PROCEDURE atualiza_vento();

/* Exercício 2 */
CREATE OR REPLACE FUNCTION atualiza_estacao() RETURNS trigger AS $$
BEGIN
	raise notice 'atualiza_estacao';
	IF ((NEW.direcao_graus > 315 AND NEW.direcao_graus < 360) OR (NEW.direcao_graus >= 0 AND NEW.direcao_graus <= 45)) THEN
		NEW.direcao_textual := 'SUL';
	ELSIF (NEW.direcao_graus <= 135) THEN
		NEW.direcao_textual := 'OESTE';
	ELSIF (NEW.direcao_graus <= 225) THEN
		NEW.direcao_textual := 'NORTE';
	ELSIF (NEW.direcao_graus <= 315) THEN
		NEW.direcao_textual := 'LESTE';
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_estacao BEFORE UPDATE ON estacao
FOR EACH ROW EXECUTE PROCEDURE atualiza_estacao();

/* Exercício 3 */
CREATE OR REPLACE FUNCTION atualiza_compra() RETURNS trigger AS $$
BEGIN
	IF (TG_OP = 'INSERT') THEN
		UPDATE cliente SET qtd_compras = qtd_compras+1 WHERE id = NEW.id_cliente;
	ELSIF (TG_OP = 'DELETE') THEN
		UPDATE cliente SET qtd_compras = qtd_compras-1 WHERE id = NEW.id_cliente;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_compra() AFTER INSERT OR DELETE ON venda
FOR EACH ROW EXECUTE PROCEDURE atualiza_compra();

CREATE OR REPLACE FUNCTION atualizar_movimentacao() RETURNS trigger AS $$
DECLARE
	troco numeric(10,2);
BEGIN
	IF (TG_OP = 'DELETE') THEN
		UPDATE produto SET qtd_estoque = qtd_estoque + OLD.quantidade WHERE id = OLD.id_produto;
	END IF;
	NEW.preco_total := NEW.quantidade * NEW.preco_unitario;
	UPDATE venda SET preco_total = NEW.preco_total WHERE id = NEW.id_venda;
	IF (TG_OP = 'INSERT') THEN
		UPDATE produto SET qtd_estoque = qtd_estoque - NEW.quantidade WHERE id = NEW.id_produto;
	ELSIF (TG_OP = 'UPDATE') THEN
		troco := NEW.quantidade - OLD.quantidade;
		IF (troco != 0) THEN
			UPDATE produto SET qtd_estoque = qtd_estoque - diferenca WHERE id = NEW.id_produto;
		END IF;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualizacao_movimentacao BEFORE INSERT OR DELETE OR UPDATE ON venda_produto
FOR EACH ROW EXECUTE PROCEDURE atualizar_movimentacao();