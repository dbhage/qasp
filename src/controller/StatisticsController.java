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

import forms.QASPFrame;
import forms.StatisticsPanel;
import session.ISessionManager;

/**
 * StatisticsController class.
 * Implements <code>Runnable</code>
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 12:53:27 PM, Nov 7, 2013 
 * Description: Controller for <code>StatisticsPanel</code>
 */
public class StatisticsController implements Runnable {

    /** The <code>StaticticsPanel</code> */
    private final StatisticsPanel statsPanel;
    
    /** The model */
    private final ISessionManager model;
    
    /** The view */
    private final QASPFrame view;

    /**
     * Constructor
     * @param qaspFrame - the view
     * @param model - the model
     */
    public StatisticsController(QASPFrame qaspFrame, ISessionManager model) {
        this.statsPanel = qaspFrame.getStatisticsPanel();
        this.model = model;
        this.view = qaspFrame;
    }

    @Override
    public void run() {
        while (statsPanel.isEnabled()) {
            // session related text fields
            statsPanel.getNoOfNodesTextField().setText(model.getNoOfNodesInCurrentSession());
            statsPanel.getSessionIdTextField().setText(model.getSessionId());
            statsPanel.getSessionStartDateTextField().setText(model.getSessionStartTime());

            // conversation related text fields
            statsPanel.getNoOfNodesTextFieldConvo().setText(model.getNoOfNodesInCurrentConversation());
            statsPanel.getConversationDurationTextField().setText(model.getCurrentConversationDuration());
            statsPanel.getConvoStartTimeTextField().setText(model.getCurrentConversationStartTime());
            statsPanel.getNoOfConversationTextField().setText(model.getNoOfConversations());
        }
    }
}