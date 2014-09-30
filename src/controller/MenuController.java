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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import forms.LoadSessionDialog;
import forms.QASPFrame;
import forms.SaveAsDialog;
import forms.SaveErrorDialog;
import session.ISessionManager;

/**
 * FileMenuController class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 3:06:50 PM, Nov 11, 2013 
 * Description: Controller for Session and Conversation menus
 */
public class MenuController {

    private final ISessionManager model;
    private final QASPFrame view;

    /**
     * Constructor
     * @param qaspFrame - the view
     * @param model - the model
     */
    public MenuController(QASPFrame qaspFrame, ISessionManager model) {
        this.model = model;
        this.view = qaspFrame;
        JMenuItem saveSessionMenuItem = qaspFrame.getSaveSessionMenuItem();
        JMenuItem loadSessionMenuItem = qaspFrame.getLoadSessionMenuItem();
        JMenuItem newSessionMenuItem = qaspFrame.getNewSessionMenuItem();
        JMenuItem endSessionMenuItem = qaspFrame.getEndSessionMenuItem();
        JMenuItem endConvoMenuItem = qaspFrame.getEndConversationMenuItem();

        saveSessionMenuItem.addActionListener(new SaveMenuActionListener());
        loadSessionMenuItem.addActionListener(new LoadMenuActionListener());
        newSessionMenuItem.addActionListener(new NewMenuActionListener());
        endSessionMenuItem.addActionListener(new EndMenuActionListener());
        endConvoMenuItem.addActionListener(new EndConversationActionListener());
    }

    // inner classes: ActionListeners for Menus.
    
    private class SaveMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.currentSessionSaved()) {
                model.saveCurrentSession();
            } else {

                // ask for session id to use to store it
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final SaveAsDialog saveAsDialog = new SaveAsDialog(new javax.swing.JFrame(), true);

                        // add action listener for save button
                        saveAsDialog.getSaveButton().addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (!saveAsDialog.getSessionIDTextField().getText().isEmpty()) {
                                    if (!model.saveSessionAs(saveAsDialog.getSessionIDTextField().getText())) {
                                        System.out.println("ERROR! Session not saved!");
                                        // <editor-fold defaultstate="collapsed" desc="show error message">
                                        java.awt.EventQueue.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                final SaveErrorDialog saveErrorDialog = new SaveErrorDialog(new javax.swing.JFrame(), true);

                                                // add action listener for ok button
                                                saveErrorDialog.getOkButton().addActionListener(new ActionListener() {

                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        saveErrorDialog.setVisible(false);
                                                    }
                                                });

                                                // add window listener for save error dialog
                                                saveErrorDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                                                    @Override
                                                    public void windowClosing(java.awt.event.WindowEvent e) {
                                                        System.exit(0);
                                                    }
                                                });
                                                saveErrorDialog.setVisible(true);
                                            }
                                        });
                                        // </editor-fold>
                                    } else {
                                        System.out.println("Saved Session!");
                                        saveAsDialog.setVisible(false);
                                    }
                                }
                            }

                        });

                        // <editor-fold defaultstate="collapsed" desc="action listener for cancel button">
                        saveAsDialog.getCancelButton().addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                saveAsDialog.setVisible(false);
                            }
                        });
                        // </editor-fold>

                        saveAsDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                        saveAsDialog.setVisible(true);
                    }
                });

            }
        }

    }

    private class LoadMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            final String[] availableSessions = model.getAvailableSessions();
            if (availableSessions == null) {
                System.out.println("No sessions to be loaded.");
                return;
            }

            // launch load session dialog box
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final LoadSessionDialog dialog = new LoadSessionDialog(new javax.swing.JFrame(), true, availableSessions);

                    // add action listener for load button
                    dialog.getLoadButton().addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // load button has been pressed

                            // get session id
                            String sid = (String) dialog.getSavedSessionsComboBox().getSelectedItem();
                            System.out.println("Loading session: " + sid);
                            model.switchToSession(sid);
                            System.out.println("Loaded session: " + sid);
                            dialog.setVisible(false);
                            view.getConversationPanel().getInputTextField().setEnabled(true);
                        }
                    });

                    // <editor-fold defaultstate="collapsed" desc="add action listener for cancel button">
                    dialog.getCancelButton().addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // cancel load, i.e. do nothing
                            // close dialog
                            dialog.setVisible(false);
                            System.out.println("Loading Cancelled!");
                        }
                    });
                    // </editor-fold>

                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                }
            });
        }
    }

    private class NewMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Creating new session.");
            model.createSession();
            view.getConversationPanel().getInputTextField().setEnabled(true);
        }

    }

    private class EndMenuActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Current Session Ended");
            model.endCurrentSession();
        }
    }

    private class EndConversationActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Current Conversation Ended!");
            model.endCurrentConversation();
        }

    }
}