package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import control.DB;
import control.TableModel;
import model.Cliente;
import model.Pedido;
import model.PedidoProduto;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import java.awt.List;
import java.awt.Color;

public class FazerPedido extends JFrame {

	private JPanel contentPane;
	private JTextField clienteBusca;
	private JTable table;
	private DefaultTableModel model;
	private JTextField textCliente;
	private JTextField pesquisaProduto;
	private JTable tableProduto;
	private JTable tableProdutoFinal;
	private JTextField fieldData;
	private double SOMA_FINAL = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FazerPedido frame = new FazerPedido();
					frame.setVisible(true);
					frame.setTitle("Venda");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FazerPedido() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 737, 585);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVenda = new JLabel("Pedido");
		lblVenda.setForeground(Color.WHITE);
		lblVenda.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblVenda.setBounds(316, 0, 93, 61);
		contentPane.add(lblVenda);
		
		clienteBusca = new JTextField();		
		clienteBusca.setBounds(534, 74, 148, 20);
		contentPane.add(clienteBusca);
		clienteBusca.setColumns(10);
		
		JButton btnNewButton = new JButton("Pesquisa");
		btnNewButton.setBounds(589, 105, 93, 23);
		contentPane.add(btnNewButton);
		
		table = new JTable();
		table.setBounds(419, 134, 263, 91);
		
		JScrollPane scrollClientes = new JScrollPane(table);
		scrollClientes.setBounds(419, 134, 263, 91);
		
		contentPane.add(scrollClientes);
		
		JButton btnFinalizar = new JButton("Finalizar");
		btnFinalizar.setBounds(545, 461, 137, 39);
		contentPane.add(btnFinalizar);
		
		ButtonGroup group = new ButtonGroup();
		
		/* DADOS */
		
		DB bd = new DB();
		
		if(bd.getConnection()) {
			String sql = "SELECT cod_cliente, nome_cliente FROM cliente ";
			
			try {
				model = TableModel.getModel(bd, sql);
				table.setModel(model);
				
			}catch(IllegalArgumentException erro) {					
				JOptionPane.showMessageDialog(null, erro.toString());
			}finally {
				bd.close();
			}
		}
		
		clienteBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(bd.getConnection()) {
					try {
						String sql = "SELECT cod_cliente, nome_cliente FROM cliente WHERE nome_cliente LIKE '%"+clienteBusca.getText()+"%' ";
						
						try {
							model = TableModel.getModel(bd, sql);
							table.setModel(model);
							
						}catch(IllegalArgumentException erro) {					
							JOptionPane.showMessageDialog(null, "Nenhum cliente encontrado");
							clienteBusca.setText("");
						}finally {
							bd.close();
						}
					}catch(NullPointerException err) {
						JOptionPane.showMessageDialog(null, "Nenhum cliente encontrado");
					}
				}
			}
		});
		
		/* ADICIONA NOME EM TEXTFIELD */
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = table.getSelectedRow();
				textCliente.setText(table.getModel().getValueAt(row, 0).toString());

			}
		});
		
		fieldData = new JTextField();
        fieldData.setBounds(242, 164, 116, 20);
        contentPane.add(fieldData);
        fieldData.setColumns(10);
		
		 Date date = Calendar.getInstance().getTime();
		 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
         String strDate = dateFormat.format(date);
         
         fieldData.setText(strDate);
         
         JLabel lblDataDoPedido = new JLabel("Data do pedido");
         lblDataDoPedido.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblDataDoPedido.setBounds(48, 156, 153, 29);
         contentPane.add(lblDataDoPedido);
         
         JLabel lblValor = new JLabel("Valor");
         lblValor.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblValor.setBounds(48, 471, 69, 29);
         contentPane.add(lblValor);
         
         JLabel lblCdigoDoCliente = new JLabel("C\u00F3digo do Cliente");
         lblCdigoDoCliente.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblCdigoDoCliente.setBounds(48, 196, 153, 29);
         contentPane.add(lblCdigoDoCliente);

         DateTimePicker dt = new DateTimePicker();
         contentPane.add(dt);
         
         textCliente = new JTextField();
         textCliente.setColumns(10);
         textCliente.setBounds(242, 203, 116, 22);
         contentPane.add(textCliente);
         
         pesquisaProduto = new JTextField();
         pesquisaProduto.setColumns(10);
         pesquisaProduto.setBounds(534, 239, 148, 20);
         contentPane.add(pesquisaProduto);
         
         JButton searchProduto = new JButton("Pesquisa");
         searchProduto.setBounds(589, 268, 93, 23);
         contentPane.add(searchProduto);
         
         tableProduto = new JTable();         
         tableProduto.setBounds(417, 302, 265, 112);
         
         JScrollPane scrollProdutos = new JScrollPane(tableProduto);
         scrollProdutos.setBounds(417, 302, 265, 112);
         
         contentPane.add(scrollProdutos);
         
         if(bd.getConnection()) {
 			String sql = "SELECT COD_PRODUTO , NOME, VALOR_UNITARIO FROM produto";
 			
 			try {
 				model = TableModel.getModel(bd, sql);
 				tableProduto.setModel(model);
 				
 			}catch(IllegalArgumentException erro) {					
 				JOptionPane.showMessageDialog(null, erro.toString());
 			}finally {
 				bd.close();
 			}
 		}
         
         JLabel lblValorTotal = new JLabel("0.00");
         lblValorTotal.setFont(new Font("Tahoma", Font.PLAIN, 26));
         lblValorTotal.setBounds(292, 467, 116, 33);
         contentPane.add(lblValorTotal);
         
         DefaultTableModel modelProdFinal = new DefaultTableModel(); 
         tableProdutoFinal = new JTable(modelProdFinal);
         tableProdutoFinal.setBounds(50, 303, 346, 111);
         
         JScrollPane scrollFinal = new JScrollPane(tableProdutoFinal);
         scrollFinal.setBounds(50, 303, 346, 111);
         
         contentPane.add(scrollFinal);
        
         
         modelProdFinal.addColumn("C�digo");
         modelProdFinal.addColumn("Nome");
         modelProdFinal.addColumn("Valor");
 
         tableProduto.addMouseListener(new MouseAdapter() {
          	@Override
          	public void mouseClicked(MouseEvent arg0) {
          		int row = tableProduto.getSelectedRow();
         
          		DefaultTableModel modelFinal = (DefaultTableModel) tableProdutoFinal.getModel();
          		
          		String codigo = tableProduto.getModel().getValueAt(row, 0).toString();
          		String nome = tableProduto.getModel().getValueAt(row, 1).toString();
          		String preco = tableProduto.getModel().getValueAt(row, 2).toString();
          		
          		modelFinal.addRow(new Object[]{codigo,nome,preco});
          		
          		int rowCount = tableProdutoFinal.getRowCount();
          		double soma = 0;
          		
          		try {
          			for(int i = 0; i < rowCount  ;i++) {
              			soma += Double.parseDouble(tableProdutoFinal.getModel().getValueAt(i, 2).toString());
              		}
          			
          			SOMA_FINAL = soma;
              		
              		lblValorTotal.setText(String.format("%.2f", SOMA_FINAL));

          		}catch(NullPointerException e) {
          			JOptionPane.showMessageDialog(null, e.toString());
          		}
          	}
          });
         

         
         JLabel lblR = new JLabel("R$");
         lblR.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblR.setBounds(258, 471, 49, 29);
         contentPane.add(lblR);
         
         JButton btnRemoverProduto = new JButton("Remover Produto");
         btnRemoverProduto.addMouseListener(new MouseAdapter() {
         	@Override
         	public void mouseClicked(MouseEvent arg0) {
         		
         		try {
         			/*if(modelProdFinal.getRowCount() > -1) {
             			modelProdFinal.removeRow(tableProdutoFinal.getSelectedRow());
             		}*/
             		
             		if(modelProdFinal.getRowCount() != -1) {
             			String codigo = modelProdFinal.getValueAt(tableProdutoFinal.getSelectedRow(), 2).toString();
                  		double valorAnterior = SOMA_FINAL;
                  		
                  		double novoValor = valorAnterior - Double.parseDouble(codigo);
                  		modelProdFinal.removeRow(tableProdutoFinal.getSelectedRow());
                  		
                  		SOMA_FINAL = novoValor;
                  		
                  		System.out.println(novoValor);
                  		
                  		if(novoValor <= 0) {
                  			lblValorTotal.setText("0");
                  		}
                  		
                  		lblValorTotal.setText(String.format("%.2f", novoValor));
             		}
         		}catch(ArrayIndexOutOfBoundsException arr) {
         			JOptionPane.showMessageDialog(null, arr.toString());
         		}
         	}
         });
         btnRemoverProduto.setBounds(48, 425, 153, 23);
         contentPane.add(btnRemoverProduto);
         
         JLabel lblClientes = new JLabel("Clientes");
         lblClientes.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblClientes.setBounds(419, 98, 69, 29);
         contentPane.add(lblClientes);
         
         JLabel lblProdutos = new JLabel("Produtos");
         lblProdutos.setFont(new Font("Tahoma", Font.PLAIN, 19));
         lblProdutos.setBounds(419, 262, 103, 29);
         contentPane.add(lblProdutos);
         
         JPanel panel = new JPanel();
         panel.setBounds(0, 0, 721, 63);
         panel.setBackground(new Color(199, 42, 27));
         contentPane.add(panel);
         
         Pedido ped = new Pedido();
         
         btnFinalizar.addMouseListener(new MouseAdapter() {
 			@Override
 			public void mouseClicked(MouseEvent e) {

 		         boolean mensagemProduto = false;
 				
 				if(textCliente.getText() == null || textCliente.getText().trim().isEmpty() || fieldData.getText() == null || fieldData.getText().trim().isEmpty() || tableProdutoFinal.getRowCount() == 0) {
 					JOptionPane.showMessageDialog(null, "Verifique se todos os campos est�o preenchidos");				
 				}else {
 					System.out.println(tableProdutoFinal.getRowCount());
 					
 					for(int i = 0; i < tableProdutoFinal.getRowCount();i++) {
 						
 						boolean salvar = ped.salvarPedido( SOMA_FINAL, fieldData.getText(), Integer.parseInt(textCliente.getText()),Integer.parseInt(modelProdFinal.getValueAt(i, 0).toString()));
 						
 	          			if(salvar) {
 	          				mensagemProduto = true; 	          				
 	          				System.out.println("done");
 	          				lblValorTotal.setText("0.00");
 	          				
 	          			}else {
 	          				mensagemProduto = false;
 	          			}
 	          		}
 					
 					JOptionPane.showMessageDialog(null, mensagemProduto ? "Pedido feito com sucesso" : "Falha ao realizar o pedido");
 					textCliente.setText("");
       				modelProdFinal.setNumRows(0);
 				}
 			}
 		});
         
        
		
	}
}
