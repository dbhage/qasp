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

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * ConversationRepresentationController class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 12:53:12 PM, Nov 7, 2013 
 * Description: Controller for <code>ConversationRepresentationPanel</code> 
 */
public class ConversationRepresentationController extends OutputStream {

    private final JTextArea metaDataTextArea;

    /**
     * Constructor
     * @param destination - the <code>JTextArea</code> where meta-data about the processes inside the 
     * system will printed.
     */
    public ConversationRepresentationController(JTextArea destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Destination is null");
        }
        this.metaDataTextArea = destination;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        final String text = new String(buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                metaDataTextArea.append(text);
            }
        });
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }
}