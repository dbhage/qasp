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

package qasp;

import forms.QASPFrame;
import session.SessionManager;
import controller.ConversationController;
import controller.ConversationRepresentationController;
import controller.MenuController;
import controller.StatisticsController;
import db.DatabaseConnection;
import db.IDatabaseConnection;
import java.io.PrintStream;
import session.ISessionManager;

/**
 * QASP class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 12:55:18 PM, Nov 7, 2013 
 * Description: Main class to launch application
 */
public class QASP {

    public QASP(QASPFrame qaspFrame) {

        // create model
        final ISessionManager model = new SessionManager();

        qaspFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                model.disconnectFromDatabase();
            }
        });

        // run statistics controller thread
        StatisticsController statisticsController = new StatisticsController(qaspFrame, model);
        Thread statisticsControllerThread = new Thread(statisticsController);
        statisticsControllerThread.start();

        // create conversation controller
        ConversationController conversationController = new ConversationController(qaspFrame, model);

        // create menu controller
        MenuController menuController = new MenuController(qaspFrame, model);

        // conversation representation controller
        ConversationRepresentationController crController
                = new ConversationRepresentationController(qaspFrame.
                        getConversationRepresentationPanel().getMetaDataTextArea());

        System.setOut(new PrintStream(crController));
    }

    public static void populateDatabase(String filename) {
        IDatabaseConnection dbConn;
        dbConn = new DatabaseConnection("qasp", 3306, "localhost", "dbhage", "qasp");
        dbConn.establishConnection();
        for (String s : util.FileUtil.readLine(filename)) {
            if (s.startsWith("INSERT")) {
                dbConn.executeInsert(s);
            }
        }
        dbConn.closeConnection();
    }

    public static void startApp() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QASPFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QASPFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QASPFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QASPFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                QASPFrame qFrame = new QASPFrame();
                qFrame.setVisible(true);
                QASP qaspApp = new QASP(qFrame);
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        QASP.startApp();
    }
}
