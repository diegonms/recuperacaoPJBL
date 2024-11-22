import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

abstract class Pessoa {
    protected String nome;
    protected String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public abstract String obterDetalhes();
}

class Cliente extends Pessoa {
    private LocalDate nascimento;

    public Cliente(String nome, String cpf, LocalDate nascimento) {
        super(nome, cpf);
        this.nascimento = nascimento;
    }

    @Override
    public String obterDetalhes() {
        return "Nome: " + nome + "\n" +
                "CPF: " + cpf + "\n" +
                "Nascimento: " + nascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate getNascimento() {
        return nascimento;
    }
}

// Classe Reserva
class Reserva {
    private Cliente cliente;
    private String tipoQuarto;
    private LocalDate checkin;
    private LocalDate checkout;
    private double valor;

    public Reserva(Cliente cliente, String tipoQuarto, LocalDate checkin, LocalDate checkout, double valor) {
        this.cliente = cliente;
        this.tipoQuarto = tipoQuarto;
        this.checkin = checkin;
        this.checkout = checkout;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return cliente.obterDetalhes() + "\n" +
                "Tipo de Quarto: " + tipoQuarto + "\n" +
                "Check-in: " + checkin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Check-out: " + checkout.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Valor: R$ " + valor + "\n" +
                "--------";
    }
}

// Gerenciamento de arquivo e reservas
class ArquivoReserva {
    private static final String CAMINHO_ARQUIVO = "reservas.txt";
    private static final ArrayList<Reserva> reservasList = new ArrayList<>();

    public static void salvarReserva(Reserva reserva) {
        reservasList.add(reserva);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {
            writer.write(reserva.toString());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar reserva: " + e.getMessage());
        }
    }

    public static String lerReservas() {
        StringBuilder reservas = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                reservas.append(linha).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler arquivo: " + e.getMessage());
        }
        return reservas.toString();
    }
}

// Tela inicial
class TelaInicial {
    public TelaInicial() {
        JFrame frame = new JFrame("Sistema de Reservas de Hotel");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnReservar = new JButton("Fazer Reserva");
        JButton btnAdm = new JButton("Área Administrativa");

        btnReservar.addActionListener(e -> new TelaCadastro());
        btnAdm.addActionListener(e -> new TelaAdm());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.add(btnReservar);
        panel.add(btnAdm);

        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// Tela de cadastro
class TelaCadastro {
    public TelaCadastro() {
        JFrame frame = new JFrame("Cadastro de Reserva");
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField nomeField = new JTextField(20);
        JTextField cpfField = new JTextField(20);
        JTextField nascimentoField = new JTextField(20);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String cpf = cpfField.getText().trim();
                LocalDate nascimento = LocalDate.parse(nascimentoField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (!cpf.matches("\\d{11}")) {
                    throw new Exception("CPF inválido! Deve conter apenas 11 dígitos.");
                }

                Cliente cliente = new Cliente(nome, cpf, nascimento);
                new TelaEscolherQuarto(cliente);
                frame.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        frame.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        frame.add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Nascimento (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        frame.add(nascimentoField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(btnSalvar, gbc);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// Tela para escolher o quarto
class TelaEscolherQuarto {
    public TelaEscolherQuarto(Cliente cliente) {
        JFrame frame = new JFrame("Escolha de Quarto");
        frame.setSize(600, 400);

        JRadioButton quartoSimples = new JRadioButton("Quarto Simples - R$150/dia");
        JRadioButton quartoLuxo = new JRadioButton("Quarto Luxo - R$300/dia");
        JRadioButton quartoPremium = new JRadioButton("Quarto Premium - R$450/dia");

        ButtonGroup grupoQuartos = new ButtonGroup();
        grupoQuartos.add(quartoSimples);
        grupoQuartos.add(quartoLuxo);
        grupoQuartos.add(quartoPremium);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener(e -> {
            try {
                String tipoQuarto;
                double valor;

                if (quartoSimples.isSelected()) {
                    tipoQuarto = "Simples";
                    valor = 150;
                } else if (quartoLuxo.isSelected()) {
                    tipoQuarto = "Luxo";
                    valor = 300;
                } else if (quartoPremium.isSelected()) {
                    tipoQuarto = "Premium";
                    valor = 450;
                } else {
                    throw new Exception("Selecione um tipo de quarto.");
                }

                Reserva reserva = new Reserva(cliente, tipoQuarto, LocalDate.now(), LocalDate.now().plusDays(1), valor);
                ArquivoReserva.salvarReserva(reserva);

                JOptionPane.showMessageDialog(frame, "Reserva confirmada: \n" + reserva.toString());
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.add(quartoSimples);
        panel.add(quartoLuxo);
        panel.add(quartoPremium);
        panel.add(btnConfirmar);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// Tela administrativa
class TelaAdm {
    public TelaAdm() {
        JFrame frame = new JFrame("Área Administrativa");
        frame.setSize(600, 400);

        JTextArea textArea = new JTextArea(ArquivoReserva.lerReservas());
        textArea.setEditable(false);

        frame.add(new JScrollPane(textArea));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

public class Sistemahoteis {
    public static void main(String[] args) {
        new TelaInicial();
    }
}
