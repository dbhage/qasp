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

package parser;

import frame.AFrame;
import memory.Memory;

/**
 * InputHandler class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:00:53 PM, Nov 7, 2013 
 * Description: Class to handle input from user
 */
public class InputHandler {

    private IParser parser;

    /**
     * Handle text from user.
     * @param text - input
     * @param pType - parser type to be used
     * @param memory - memory
     */
    public void handleText(String text, ParserType pType, Memory memory) {
        if (pType == null) {
            System.err.println("Parser type is null.");
            System.exit(-1);
        } else if (pType.equals(ParserType.QUERY)) {
            System.out.println("-----------------------------");
            System.out.println("Parsing query.");
            parser = new QueryParser(memory);
            parser.parse(text);
            System.out.println("Generated Frames:");
            for (AFrame frame : getFrames()) {
                System.out.println(frame.toString());
            }
            System.out.println("-----------------------------");
        } else if (pType.equals(ParserType.SENTENCE)) {
            System.out.println("-----------------------------");
            System.out.println("Parsing sentence.");
            parser = new SentenceParser(memory);
            parser.parse(text);
            System.out.println("-----------------------------");
        }
    }

    public AFrame[] getFrames() {
        if (parser instanceof QueryParser) {
            return ((QueryParser) parser).getFrames();
        }
        return null;
    }
}