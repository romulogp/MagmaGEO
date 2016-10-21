package br.com.hustik.magmageo.database;

import br.com.hustik.magmageo.model.TipoOcorrencia;
import br.com.hustik.magmageo.model.kml.KMLPolygon;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rômulo Goelzer Portolann
 */

public class BancoDeDados {

    private final String JDBC;
    private final String HOST;
    private final String PORT;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;
    private final String URL;
    private final String SRID;
    public  Connection conexao;
    private boolean conectado;

    public BancoDeDados() {
        /* Constantes para conexão ao SGBD */
        this.JDBC      = "postgresql";
        this.HOST      = "//localhost";
        this.PORT      = "5432";
        this.DATABASE  = "db_magmageo";
        this.USER      = "user_magmageo";
        this.PASSWORD  = "magmageo";
        this.URL       = "jdbc:" + JDBC + ":" + HOST + ":" + PORT + "/" + DATABASE;
        this.SRID      = "4326";
        this.conectado = false;
    }

    public boolean ConectaBanco() {
        try {
            this.conexao = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
            this.conectado = true;
            return true;
        } catch (Exception e) {
            System.out.println("Nao foi possivel conectar ao banco de dados!" + e.getMessage());
            return false;
        }
    }

    public boolean isConnected() {
        return conectado;
    }

    public boolean DesconectaBanco() throws SQLException {
        try {
            this.conexao.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
//    public Atendente autenticarAtendente(String login, String senha) {
//        try {
//            /* Previne SQL Injection */
//            String SQL = "SELECT id, nome FROM atendente WHERE login=? AND senha=MD5(?);";
//            PreparedStatement prepStmt = conexao.prepareStatement(SQL);
//            prepStmt.setString(1, login);
//            prepStmt.setString(2, senha);
//            ResultSet rs = prepStmt.executeQuery();
//
//            int linhasConsulta = countResultSetLines(rs);
//
//            int id = 0;
//            String nome = null;
//            if (linhasConsulta == 1) {
//                rs = prepStmt.executeQuery();
//                rs.next();
//                id = rs.getInt("id");
//                nome = rs.getString("nome");
//            }
//            return new Atendente(id, nome, login, senha);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "ERRO SQL " + e.getMessage());
//        }
//        System.out.println("Erro ao autenticar usuário");
//        System.exit(100);
//        return null;
//    }

    public boolean cadastroBairro(String bairro, String geometria) {
        try {
            if (ConectaBanco()) {
//                conexao.setAutoCommit(false);
                String SQL = "INSERT INTO bairro(nome, geometria) "
                        + "VALUES (?, "
                        + "ST_GeomFromText(?, "
                        + "4326));";
                PreparedStatement prepStmt = conexao.prepareStatement(SQL);
                prepStmt.setString(1, bairro);
                prepStmt.setString(2, "POLYGON((" + geometria + "))");
                prepStmt.execute();
//                conexao.commit();
                DesconectaBanco();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    public boolean cadastroTipoOcorrencia(String descricao) {
        try {
            ConectaBanco();
            String SQL = "INSERT INTO tipo (descricao) VALUES (?);";
            PreparedStatement prepStmt = conexao.prepareStatement(SQL);
            prepStmt.setString(1, descricao);
            prepStmt.executeUpdate();
            DesconectaBanco();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar tipo");
            return false;
        }
        return true;
    }
    
    public boolean cadastroOcorrencia(String descricao, int id_tipo, String geometria) {
        try {
            if (ConectaBanco()) {
//                conexao.setAutoCommit(false);
                String SQL = "INSERT INTO ocorrencia(descricao, id_tipo, geometria) "
                        + "VALUES (?, "
                        + "?, "
                        + "ST_GeomFromText(?, 4326));";
                PreparedStatement prepStmt = conexao.prepareStatement(SQL);
                prepStmt.setString(1, descricao);
                prepStmt.setInt   (2, id_tipo);
                prepStmt.setString(3, "POINT(" + geometria + ")");
                prepStmt.execute();
//                conexao.commit();
                DesconectaBanco();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    public DefaultTableModel atualizaListaPercentualOcorrencia(DefaultTableModel model) {
        try {
            /* Obtém o CABEÇALHO da tabela */
            ConectaBanco();
            Statement st = conexao.createStatement();
            String SQL = "SELECT bairro AS \"Bairro\", percentual AS \"Percentual (%)\" FROM percentual_ocorrencias_por_bairro() ORDER BY percentual DESC;";
            ResultSet rs = st.executeQuery(SQL);
            ResultSetMetaData rsMeta = rs.getMetaData();
            String[] header = new String[rsMeta.getColumnCount()];
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                header[i] = rsMeta.getColumnName(i + 1);
            }

            /* Adiciona coluna no modelo */
            for (String titulo : header) {
                model.addColumn(titulo);
            }

            /* Obtém os DADOS da tabela */
            rs = st.executeQuery(SQL);
            /* Obter o número de linhas */
            int nLinhas = 0;
            while (rs.next()) {
                nLinhas++;
            }

            rs = st.executeQuery(SQL);
            rsMeta = rs.getMetaData();

            /* Obtém o número de colunas */
            int nColunas = rsMeta.getColumnCount();

            /* Cria Matriz para os dados */
            String[][] matriz = new String[nLinhas][nColunas];
            for (int i = 0; i < nLinhas; i++) { //Obter Dados das colunas
                rs.next();
                for (int j = 0; j < nColunas; j++) {
                    matriz[i][j] = rs.getString(j + 1);
                    //System.out.println(matriz[i][j]);
                }
            }
            String[] linhaDados;
            for (String[] data1 : matriz) {   //Insere os dados na tabela
                linhaDados = new String[header.length];
                System.arraycopy(data1, 0, linhaDados, 0, header.length);
                model.addRow(linhaDados);
            }

            DesconectaBanco();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar cabeçalhos");
            System.exit(200);
        }
        return model;
    }

    public List<String> ObterCatalago() throws SQLException {
        List<String> nomeDaBase = new ArrayList<>();
        DatabaseMetaData dbMeta = conexao.getMetaData();
        ResultSet rsCatalogs = dbMeta.getCatalogs();

        while (rsCatalogs.next()) {
            nomeDaBase.add(rsCatalogs.getString("TABLE_CAT"));
        }
        return nomeDaBase;
    }

    public List<String> ObterTabela(String nomeDaBase) throws SQLException {
        List<String> nomeDaTabela = new ArrayList<>();
        DatabaseMetaData dbMeta = conexao.getMetaData();
        ResultSet rsTables = dbMeta.getTables(nomeDaBase, null, null, null);
        while (rsTables.next()) {
            nomeDaTabela.add(rsTables.getString("TABLE_NAME"));
        }
        return nomeDaTabela;
    }

    public String[][] ObterColuna(String baseDeDados, String tabela) throws SQLException {
        //Cria Instância para Execução SQL
        Statement st = conexao.createStatement();
        //Código SQL a ser executado
        String SQL = "SELECT * FROM " + baseDeDados + "." + tabela;
        //Executa SQL e obtém o conjunto de dados
        ResultSet rs = st.executeQuery(SQL);
        //Obtém maiores informações do ResultSet, (número de colunas, nome das colunas)
        ResultSetMetaData rsMeta = rs.getMetaData();
        //Obtém o número de linhas
        int nLinhas = 0;
        while (rs.next()) {
            nLinhas++;
        }
        //Restaura Ponteiro SQL
        rs.beforeFirst();
        //Obtém o número de colunas
        int nColunas = rsMeta.getColumnCount();
        //Cria Matriz
        String[][] matriz = new String[nLinhas][nColunas];
        //Obtém Dados das colunas
        for (int i = 0; i < nLinhas; i++) {
            rs.next();
            for (int j = 0; j < nColunas; j++) {
                matriz[i][j] = rs.getString(j + 1);
            }
        }
        return matriz;
    }

    public ResultSet ObterResultSetColuna(String baseDeDados, String nomeDaTabela) throws SQLException {

        Statement st = conexao.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        String SQL = "SELECT * FROM " + baseDeDados + "." + nomeDaTabela;
        ResultSet rs = st.executeQuery(SQL);
        return rs;
    }

    public DefaultTableModel atualizaListaOcorrencias(DefaultTableModel model) {
        try {
            /* Obtém o CABEÇALHO da tabela */
            ConectaBanco();
            Statement st = conexao.createStatement();
            String SQL = "SELECT o.id as \"ID\", t.descricao as \"Tipo\", o.descricao as \"Descrição\", b.nome as \"Bairro\"" +
                "FROM ocorrencia o LEFT JOIN bairro b " +
                "ON (o.id_bairro = b.id) INNER JOIN tipo t " +
                "ON (o.id_tipo = t.id) " +
                "ORDER BY b.nome;";
            ResultSet rs = st.executeQuery(SQL);
            ResultSetMetaData rsMeta = rs.getMetaData();
            String[] header = new String[rsMeta.getColumnCount()];
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                header[i] = rsMeta.getColumnName(i + 1);
            }

            /* Adiciona coluna no modelo */
            for (String titulo : header) {
                model.addColumn(titulo);
            }

            /* Obtém os DADOS da tabela */
            rs = st.executeQuery(SQL);
            /* Obter o número de linhas */
            int nLinhas = 0;
            while (rs.next()) {
                nLinhas++;
            }

            rs = st.executeQuery(SQL);
            rsMeta = rs.getMetaData();

            /* Obtém o número de colunas */
            int nColunas = rsMeta.getColumnCount();

            /* Cria Matriz para os dados */
            String[][] matriz = new String[nLinhas][nColunas];
            for (int i = 0; i < nLinhas; i++) { //Obter Dados das colunas
                rs.next();
                for (int j = 0; j < nColunas; j++) {
                    matriz[i][j] = rs.getString(j + 1);
                    //System.out.println(matriz[i][j]);
                }
            }
            String[] linhaDados;
            for (String[] data1 : matriz) {   //Insere os dados na tabela
                linhaDados = new String[header.length];
                System.arraycopy(data1, 0, linhaDados, 0, header.length);
                model.addRow(linhaDados);
            }

            DesconectaBanco();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar cabeçalhos");
            System.exit(200);
        }
        return model;
    }

    public DefaultTableModel atualizaListaBairros(DefaultTableModel model) {
        try {
            /* Obtém o CABEÇALHO da tabela */
            ConectaBanco();
            Statement st = conexao.createStatement();
            String SQL = "SELECT id as \"ID\", nome as \"Bairro\", st_astext(geometria) as \"Geometria\" FROM bairro;";
            ResultSet rs = st.executeQuery(SQL);
            ResultSetMetaData rsMeta = rs.getMetaData();
            String[] header = new String[rsMeta.getColumnCount()];
            for (int i = 0; i < rsMeta.getColumnCount(); i++) {
                header[i] = rsMeta.getColumnName(i + 1);
            }

            /* Adiciona coluna no modelo */
            for (String titulo : header) {
                model.addColumn(titulo);
            }

            /* Obtém os DADOS da tabela */
            rs = st.executeQuery(SQL);
            /* Obter o número de linhas */
            int nLinhas = 0;
            while (rs.next()) {
                nLinhas++;
            }

            rs = st.executeQuery(SQL);
            rsMeta = rs.getMetaData();

            /* Obtém o número de colunas */
            int nColunas = rsMeta.getColumnCount();

            /* Cria Matriz para os dados */
            String[][] matriz = new String[nLinhas][nColunas];
            for (int i = 0; i < nLinhas; i++) { //Obter Dados das colunas
                rs.next();
                for (int j = 0; j < nColunas; j++) {
                    matriz[i][j] = rs.getString(j + 1);
                    if (matriz[i][j].equals("f")) {
                        matriz[i][j] = "Não";
                    } else if (matriz[i][j].equals("t")) {
                        matriz[i][j] = "Sim";
                    }
                }
            }
            String[] linhaDados;
            for (String[] data1 : matriz) {   //Insere os dados na tabela
                linhaDados = new String[header.length];
                System.arraycopy(data1, 0, linhaDados, 0, header.length);
                model.addRow(linhaDados);
            }
            DesconectaBanco();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar cabeçalhos");
            System.exit(200);
        }
        return model;
    }
    
    
    public ArrayList<KMLPolygon> obterKMLDosBairros() {
        ArrayList<KMLPolygon> lista = new ArrayList<>();
        ConectaBanco();
        try {
            if (isConnected()) {
                Statement st = conexao.createStatement();
                String SQL = "SELECT kml, bairro, percentual FROM funcao_kml_bairro_porcent();";
                ResultSet rs = st.executeQuery(SQL);

                String kmlPolygon;
                String bairro;
                float percentual;
                while (rs.next()) {
                    kmlPolygon = rs.getString("kml");
                    bairro  = rs.getString("bairro");
                    percentual = rs.getFloat("percentual");
                    lista.add(new KMLPolygon(kmlPolygon, bairro, percentual));
                }
                DesconectaBanco();
            }
        } catch (Exception e) {
            System.out.println("Ocorrreu um erro ao carregar os dados KML.");
        }
        return lista;
    }
    
    public ArrayList<TipoOcorrencia> getListaTipos() {
        ArrayList<TipoOcorrencia> lista = new ArrayList<>();
        ConectaBanco();
        try {
            if (isConnected()) {
                Statement st = conexao.createStatement();
                String SQL = "SELECT id, descricao FROM tipo";
                ResultSet rs = st.executeQuery(SQL);

                int id;
                String descricao;
                while (rs.next()) {
                    id = rs.getInt("id");
                    descricao = rs.getString("descricao");

                    lista.add(new TipoOcorrencia(id , descricao));
                }
                DesconectaBanco();
            }
        } catch (Exception e) {
            System.out.println("Ocorrreu um erro ao carregar a relação de tipos de ocorrência");
        }
        return lista;
    }

    public boolean cadastroChamado(int id_atendente, int id_cliente, int id_categoria, String descricao, Timestamp dateTime) {
        try {
            ConectaBanco();
            String SQL = "INSERT INTO chamado (id_atendente, id_cliente, id_categoria, descricao, data_hora) " + ""
                    + "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement prepStmt = conexao.prepareStatement(SQL);
            prepStmt.setInt(1, id_atendente);
            prepStmt.setInt(2, id_cliente);
            prepStmt.setInt(3, id_categoria);
            prepStmt.setString(4, descricao);
            prepStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            //prepStmt.setTimestamp(5, dateTime); // Não insere no BD (sei lá por que)
//            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            String sdt = df.format(new Date(System.currentTimeMillis()));
//            System.out.println(sdt);

            prepStmt.executeUpdate();
            DesconectaBanco();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Chamado. " + ex);
            return false;
        }
        return true;
    }

    public String[] ObterCabecalho(String baseDeDados, String tabela) throws SQLException {
        Statement st = conexao.createStatement();
        String SQL = "SELECT * FROM " + baseDeDados + "." + tabela;
        ResultSet rs = st.executeQuery(SQL);
        ResultSetMetaData rsMeta = rs.getMetaData();
        String[] header = new String[rsMeta.getColumnCount()];
        for (int i = 0; i < rsMeta.getColumnCount(); i++) {
            header[i] = rsMeta.getColumnName(i + 1) + " (" + rsMeta.getColumnTypeName(i + 1) + ")";
        }
        return header;
    }

    public ResultSet SelectAllFrom(String baseDeDados, String tabela) throws SQLException {
        Statement st = conexao.createStatement();
        String SQL = "SELECT * FROM " + baseDeDados + "." + tabela;
        ResultSet rs = st.executeQuery(SQL);
        return rs;
    }


    
    public boolean InsertInto(String baseDeDados, String tabela, String[] rowData) {
        String SQL = "INSERT INTO " + baseDeDados + "." + tabela + " VALUES ";
        for (int i = 0; i < rowData.length - 1; i++) {
            SQL += "'" + rowData[i] + "', ";
        }
        SQL += rowData[rowData.length];
        System.out.println(SQL);
        return true;
    }

}
