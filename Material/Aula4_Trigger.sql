/* Exercício 1
	Crie uma trigger que evite que sejam inseridos dependentes (na tabela dependentes) 
	maiores de 18 anos com sexo masculino. 
*/

CREATE OR REPLACE FUNCTION bloquear_dependende_maioridade() RETURNS trigger AS $$
BEGIN
	IF (TG_OP = 'INSERT') THEN
		IF (age(NEW.data_nascimento) >= '18 years' AND upper(NEW.sexo) = 'M') THEN
			RAISE EXCEPTION 'Não é possível inserir dependentes com idade maior ou igual a 18 anos';
		END IF;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER menoridade_masculina BEFORE INSERT OR UPDATE ON dependente
FOR EACH ROW EXECUTE PROCEDURE bloquear_dependende_maioridade();


/* Exercício 2
	Crie uma trigger que evite que um empregado seja supervisor dele mesmo. 
*/
CREATE OR REPLACE FUNCTION bloquear_superior_si_proprio() RETURNS trigger AS $$
BEGIN
	IF (NEW.rg = NEW.rg_supervisor) THEN
		RAISE EXCEPTION 'Supervisor inválido';
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER bloqueia_superior_si_proprio BEFORE INSERT OR UPDATE ON empregado
FOR EACH ROW EXECUTE PROCEDURE bloquear_superior_si_proprio();


/* Exercício 3
	Crie uma trigger que faça exclusão em cascata todas as vezes que um projeto
	for excluído do banco de dados. 
*/
CREATE OR REPLACE FUNCTION excluir_projeto_cascata() RETURNS trigger AS $$
BEGIN
	DELETE FROM empregado_projeto WHERE id_projeto = OLD.id;
	DELETE FROM departamento_projeto WHERE id_projeto = OLD.id;
	RETURN OLD;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER delete_projeto_cascata BEFORE DELETE ON empregado
FOR EACH ROW EXECUTE PROCEDURE excluir_projeto_cascata();


/* Exercício 4
	Adicione o atributo mês (atributo do tipo inteiro) na tabela Empregado_Projeto. 
	Em seguida, crie uma trigger que seja disparada sempre que um registro for inserido
	/atualizado na tabela Empregado_Projeto. Essa trigger deve verificar se o número
	total de horas de um funcionário (fazer o somatório do número de horas para o 
	empregado) no mês corrente não excede 180 horas. Se exceder, o registro não 
	poderá ser inserido/atualizado. 
*/
CREATE OR REPLACE FUNCTION entrar_num_projeto() RETURNS trigger AS $$
DECLARE
	total_horas integer;
BEGIN
	SELECT INTO total_horas SUM(horas) FROM empregado_projeto WHERE rg = NEW.rg AND mes = NEW.mes;
	IF (total_horas + NEW.horas >= 180) THEN
		RAISE EXCEPTION 'Excede total de horas';
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER entra_num_projeto BEFORE INSERT OR UPDATE ON empregado_projeto
FOR EACH ROW EXECUTE PROCEDURE entrar_num_projeto();


/* Exercício 5
	Crie uma trigger que, ao vincular um empregado a um projeto, atualize o valor do salário do
	empregado (adicione R$50,00 ao salário) todas as vezes que o mesmo se envolver em um novo
	projeto.
*/
CREATE OR REPLACE FUNCTION atualizar_salario() RETURNS trigger AS $$
DECLARE
	estaNoProjeto integer;
BEGIN
	SELECT INTO estaNoProjeto COUNT(*) FROM empregado_projeto WHERE id_projeto = NEW.id_projeto;
	IF (estaNoProjeto = 0) THEN
		UPDATE empregado SET salario = salario + 50 WHERE rg = NEW.rg;
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER atualiza_salario AFTER INSERT ON empregado_projeto
FOR EACH ROW EXECUTE PROCEDURE atualizar_salario();

