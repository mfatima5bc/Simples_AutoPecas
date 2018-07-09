package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BEAN.Produto;

/**
 *
 * @author Fátima
 */
public class ProdutoDAO {

    private Connection connection;
    private PreparedStatement stmt;
    private String sql;

    public ProdutoDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void Adicionar(Produto p) {
        connection = new ConnectionFactory().getConnection();

        sql = "insert into produtos(nome, descricao, preco, codBarra, qtd) values(?,?,?,?,?)";
        try {
            stmt = connection.prepareCall(sql);
            stmt.setString(1, p.getNome().getValue());
            stmt.setString(2, p.getDescricao().getValue());
            stmt.setDouble(3, p.getPreco().getValue());
            stmt.setString(4, p.getCodBarra().getValue());
            stmt.setInt(5, p.getQtd().getValue());
            stmt.execute();
            System.out.println("Adiconado com sucesso!");
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("erro de add " + e);
        }
    }

    public ObservableList<Produto> gerarLista() {
        connection = new ConnectionFactory().getConnection();
        ObservableList<Produto> Lista
                = FXCollections.observableArrayList();

        try {
            stmt = connection.prepareStatement("select * from produtos;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lista.add(new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("codBarra"),
                        rs.getInt("qtd"),
                        rs.getLong("id"))
                );
            }
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Lista;
    }
    
        public ObservableList<Produto> FiltrarLista(String n) {
        connection = new ConnectionFactory().getConnection();
        ObservableList<Produto> Lista
                = FXCollections.observableArrayList();

        try {
            stmt = connection.prepareStatement("select * from produtos where nome like ?;");
            stmt.setString(1, "%" + n + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lista.add(new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("codBarra"),
                        rs.getInt("qtd"),
                        rs.getLong("id"))
                );
            }
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Lista;
    }

    public void remover(long id) throws SQLException {
        connection = new ConnectionFactory().getConnection();

        sql = "delete from produtos where id = ?";
        stmt = connection.prepareStatement(sql);
        // seta os valores
        stmt.setLong(1, id);
        // executa
        stmt.execute();
        System.out.println("Excluido com sucesso!");
        stmt.close();
    }

    public void update(Produto c) {
        connection = new ConnectionFactory().getConnection();

        try {
            stmt = connection.prepareStatement("UPDATE produtos SET nome = ?, descricao = ?, preco = ?, codBarra = ?, qtd = ? WHERE id = ?;");
            stmt.setString(1, c.getNome().getValue());
            stmt.setString(2, c.getDescricao().getValue());
            stmt.setDouble(3, c.getPreco().getValue());
            stmt.setString(4, c.getCodBarra().getValue());
            stmt.setInt(5, c.getQtd().getValue());
            stmt.setLong(6, c.getId().longValue());
            stmt.executeUpdate();
            connection.close();
            stmt.close();
            System.out.println("\n Produto Atualizado!\n");
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String PegarDescricao(long id) {
        connection = new ConnectionFactory().getConnection();
        Produto p = new Produto();
        try {
            stmt = connection.prepareStatement("select * from produtos WHERE id = ?;");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                p = new Produto(
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getString("codBarra"),
                        rs.getInt("qtd"),
                        rs.getLong("id")
                );

            }
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p.getDescricao().getValue();
    }

}