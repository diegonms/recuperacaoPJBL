import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Classe abstrata Pessoa (classe abstrata com método abstrato)
abstract class Pessoa {
    protected String nome;
    protected String cpf;
    protected String endereco;

    // Construtor da classe Pessoa
    public Pessoa(String nome, String cpf, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    // Método abstrato que deve ser implementado pelas subclasses
    public abstract String obterDetalhes();

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}

// Subclasse Cliente, herda de Pessoa e implementa o método abstrato obterDetalhes()
class Cliente extends Pessoa {
    private String telefone;
    private String dataNascimento;
    private String email; // Novo atributo

    public Cliente(String nome, String cpf, String endereco, String telefone, String dataNascimento, String email) {
        super(nome, cpf, endereco); // Chama o construtor da classe Pai
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    // Implementação do método abstrato
    @Override
    public String obterDetalhes() {
        return "Cliente: " + nome + ", CPF: " + cpf + ", Telefone: " + telefone + ", Data de Nascimento: " + dataNascimento + ", Endereço: " + endereco + ", Email: " + email;
    }

    // Getters e Setters
    public String getTelefone() {
        return telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// Subclasse Funcionario, herda de Pessoa e implementa o método abstrato obterDetalhes()
class Funcionario extends Pessoa {
    private String cargo;
    private double salario; // Novo atributo

    public Funcionario(String nome, String cpf, String endereco, String cargo, double salario) {
        super(nome, cpf, endereco); // Chama o construtor da classe Pai
        this.cargo = cargo;
        this.salario = salario;
    }

    // Implementação do método abstrato
    @Override
    public String obterDetalhes() {
        return "Funcionário: " + nome + ", CPF: " + cpf + ", Cargo: " + cargo + ", Salário: " + salario + ", Endereço: " + endereco;
    }

    // Getters e Setters
    public String getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}

// Classe de Exceção Personalizada para validação de dados
class ValidacaoException extends Exception {
    public ValidacaoException(String mensagem) {
        super(mensagem); // Chama o construtor da classe Exception
    }
}

// Classe Reserva (com associação com Cliente)
class Reserva {
    private String checkIn;
    private String checkOut;
    private String tipoQuarto;
    private Cliente cliente;
    private double preco;
    private String pagamento; // Novo atributo para associação

    public Reserva(String checkIn, String checkOut, String tipoQuarto, Cliente cliente, double preco, String pagamento) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.tipoQuarto = tipoQuarto;
        this.cliente = cliente; // Associação com a classe Cliente
        this.preco = preco;
        this.pagamento = pagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public String getTipoQuarto() {
        return tipoQuarto;
    }

    public double getPreco() {
        return preco;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}

// Sistema de Reservas (com relação de associação com Reserva)
class SistemaDeReservas {
    private List<Reserva> reservas = new ArrayList<>();
    private final String caminhoArquivo = "reservas.csv";

    public SistemaDeReservas() {
        carregarReservasDeArquivo(); // Carrega as reservas já feitas ao iniciar o sistema
    }

    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva); // Adiciona a reserva à lista
        salvarReservasEmArquivo(); // Salva no arquivo CSV
    }

    public void salvarReservasEmArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            for (Reserva reserva : reservas) {
                // Salva informações de reserva no arquivo
                writer.write(reserva.getCliente().getNome() + "," + reserva.getCliente().getCpf() + ","
                        + reserva.getCheckIn() + "," + reserva.getCheckOut() + "," + reserva.getTipoQuarto() + ","
                        + reserva.getPreco() + "," + reserva.getPagamento() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Trata a exceção de IO
        }
    }

    public void carregarReservasDeArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                // Lê dados do arquivo e os usa para criar reservas
                String[] partes = linha.split(",");
                if (partes.length < 7) { // Validação para garantir que os dados estão completos
                    System.out.println("Linha ignorada por estar incompleta ou mal formatada: " + linha);
                    continue;
                }

                String nome = partes[0];
                String cpf = partes[1];
                String checkIn = partes[2];
                String checkOut = partes[3];
                String tipoQuarto = partes[4];
                double preco = Double.parseDouble(partes[5]);
                String pagamento = partes[6];

                Cliente cliente = new Cliente(nome, cpf, "Desconhecido", "Desconhecido", "Desconhecido", "Desconhecido");
                Reserva reserva = new Reserva(checkIn, checkOut, tipoQuarto, cliente, preco, pagamento);
                reservas.add(reserva); // Adiciona a reserva ao sistema
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar reservas: " + e.getMessage());
        }
    }
}

// Interface Gráfica
class HotelReservationSystemGUI {
    private SistemaDeReservas sistema;

    public HotelReservationSystemGUI() {
        sistema = new SistemaDeReservas(); // Cria uma instância do sistema de reservas
    }

    public void mostrarTelaPrincipal() {
        JFrame frame = new JFrame("Sistema de Reservas");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Criação do botão de reserva
        JButton reservaButton = new JButton("Fazer Reserva");
        reservaButton.addActionListener(e -> mostrarTelaReserva(frame));

        frame.add(new JLabel("Bem-vindo ao Sistema de Reservas do Hotel!", SwingConstants.CENTER), BorderLayout.CENTER);
        frame.add(reservaButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Tela de reserva com os campos necessários
    public void mostrarTelaReserva(JFrame frame) {
        JFrame reservaFrame = new JFrame("Fazer Reserva");
        reservaFrame.setSize(400, 400);
        reservaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Usando GridBagLayout para melhorar o alinhamento dos campos
        reservaFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre os componentes
        gbc.anchor = GridBagConstraints.WEST; // Alinhar à esquerda

        JTextField nomeField = new JTextField(20);
        JTextField dataNascimentoField = new JTextField(20);
        JTextField cpfField = new JTextField(20);
        JTextField checkInField = new JTextField(20);
        JTextField checkOutField = new JTextField(20);
        JTextField telefoneField = new JTextField(20); // Novo campo
        JTextField enderecoField = new JTextField(20); // Novo campo
        String[] tiposDeQuarto = {"Simples", "Médio", "Grande"};
        JComboBox<String> tipoQuartoComboBox = new JComboBox<>(tiposDeQuarto);

        JButton finalizarButton = new JButton("Finalizar Reserva");
        finalizarButton.addActionListener(e -> {
            try {
                // Validação do CPF
                String nome = nomeField.getText();
                String cpf = cpfField.getText();
                if (!Pattern.matches("\\d{11}", cpf)) {
                    throw new ValidacaoException("CPF inválido!");
                }

                // Criação da reserva
                Cliente cliente = new Cliente(nome, cpf, "Desconhecido", telefoneField.getText(), dataNascimentoField.getText(), "email@exemplo.com");
                Reserva reserva = new Reserva(checkInField.getText(), checkOutField.getText(), (String) tipoQuartoComboBox.getSelectedItem(), cliente, 200.00, "Cartão de Crédito");
                sistema.adicionarReserva(reserva); // Adiciona a reserva ao sistema

                JOptionPane.showMessageDialog(reservaFrame, "Reserva realizada com sucesso!");
            } catch (ValidacaoException ex) {
                JOptionPane.showMessageDialog(reservaFrame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        reservaFrame.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        reservaFrame.add(new JLabel("Data Nascimento:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(dataNascimentoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        reservaFrame.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        reservaFrame.add(new JLabel("Check-in:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(checkInField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        reservaFrame.add(new JLabel("Check-out:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(checkOutField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        reservaFrame.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(telefoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        reservaFrame.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(enderecoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        reservaFrame.add(new JLabel("Tipo Quarto:"), gbc);
        gbc.gridx = 1;
        reservaFrame.add(tipoQuartoComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        reservaFrame.add(finalizarButton, gbc);

        reservaFrame.setVisible(true);
    }
}

// Função principal
public class Sistemahoteis {
    public static void main(String[] args) {
        HotelReservationSystemGUI gui = new HotelReservationSystemGUI();
        gui.mostrarTelaPrincipal();
    }
}

//sobreescrito: O método obterDetalhes() é sobrescrito nas subclasses Cliente e Funcionario.
//polimorfismo: ocorre quando as subclasses Cliente e Funcionario implementam o método obterDetalhes() de forma diferente.