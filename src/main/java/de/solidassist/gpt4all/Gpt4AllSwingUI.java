package de.solidassist.gpt4all;

import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Gpt4AllSwingUI {

    private static ChatBotService chatBotService;

    public static void main(String[] args) {
        // Initialize the chatbot service
        chatBotService = new ChatBotService(new RestTemplate());

        // Create the frame
        JFrame frame = new JFrame("My first local AI chat bot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null); // Center the window

        // Create components
        JTextPane chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(chatPane);

        JTextArea inputArea = new JTextArea(3, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane inputScroll = new JScrollPane(inputArea);

        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton clearButton = new JButton("Clear Chat");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Styles for different message types
        StyledDocument doc = chatPane.getStyledDocument();

        Style userStyle = doc.addStyle("UserStyle", null);
        StyleConstants.setBackground(userStyle, new Color(220, 248, 198));
        StyleConstants.setFontSize(userStyle, 16);
        StyleConstants.setFontFamily(userStyle, "Arial");
        StyleConstants.setBold(userStyle, true);

        Style botStyle = doc.addStyle("BotStyle", null);
        StyleConstants.setBackground(botStyle, new Color(240, 240, 240));
        StyleConstants.setFontSize(botStyle, 16);
        StyleConstants.setFontFamily(botStyle, "Arial");

        // Create layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(clearButton, BorderLayout.WEST);
        buttonPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(inputPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        // Button action to send message
        sendButton.addActionListener((ActionEvent e) -> {
            String userMessage = inputArea.getText().trim();
            if (!userMessage.isEmpty()) {
                try {
                    doc.insertString(doc.getLength(), "You: " + userMessage + "\n", userStyle);
                    doc.insertString(doc.getLength(), "\n", null); // Space between messages

                    String response = chatBotService.getResponse(userMessage);
                    doc.insertString(doc.getLength(), "Bot: " + response + "\n\n", botStyle);
                    chatPane.setCaretPosition(doc.getLength());
                    inputArea.setText("");
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Button action to clear chat
        clearButton.addActionListener((ActionEvent e) -> {
            chatPane.setText("");
        });
    }
}
