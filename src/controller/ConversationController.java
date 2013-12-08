/*
 * Copyright (c) 2013, Dwijesh Bhageerutty
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package controller;

import forms.AskForWordDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import session.SessionManager;
import forms.ConversationPanel;
import forms.QASPFrame;
import javax.swing.WindowConstants;

/**
 * ConversationController class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 12:52:27 PM, Nov 7, 2013 
 * Description:
 */
public class ConversationController {

    private final ConversationPanel conversationPanel;
    private final QASPFrame view;
    private final SessionManager model;

    public ConversationController(QASPFrame qaspFrame, SessionManager model) {
        this.view = qaspFrame;
        this.conversationPanel = qaspFrame.getConversationPanel();
        this.model = model;

        this.conversationPanel.getInputTextField().addActionListener(new InputTextFieldActionListener());
    }

    private class InputTextFieldActionListener implements ActionListener {
        
        private AskForWordDialog dialog;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = conversationPanel.getInputTextField().getText();
            conversationPanel.getInputTextField().setText("");
            conversationPanel.getConversationTextArea().append("> " + text + "\n");

            checkForWordsExistence(text);
            
            if (text.endsWith("?")) {
                conversationPanel.getConversationTextArea().append("> " + model.handleQuery(text) + "\n");
            } else {
                model.handleText(text);
            }
        }

        private void checkForWordsExistence(String text) {
            String[] words = text.split("[^a-zA-Z]+");
            
            if (!(words.length > 2)) {
                System.err.println("Sentence length is not greater than 2.");
                System.exit(-1);
            }
            
            for (String word : words) {
                System.out.println(word);
                if (!model.wordExists(word)) {
                    // show get word dialog box
                    dialog = new AskForWordDialog(view, true, word);
                    dialog.getSubmitButton().addActionListener(new SubmitButtonActionListener());
                    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    dialog.setVisible(true);
                }
            }
        }
        
        private class SubmitButtonActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String definition = dialog.getDefinitionTextField().getText();
                String pos = (String) dialog.getPosComboBox().getSelectedItem();
                model.saveNewWord(dialog.getWordLabel().getText(), definition, pos);       
                dialog.setVisible(false);
            }
        }
    }
}