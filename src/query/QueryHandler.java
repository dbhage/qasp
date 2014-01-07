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
package query;

import query.cma.ICMA;
import frame.AFrame;
import java.util.ArrayList;
import java.util.List;
import memory.Memory;
import query.cma.CMA1;
import query.cma.CMA2;
import query.cma.CMAType;

/**
 * QueryHandler class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:08:29 PM Nov
 * 7, 2013 Description:
 */
public class QueryHandler {

    private ICMA icma;
    private List<QueryResult> queryResults;

    public void handleQuery(AFrame[] frames, CMAType cType, Memory memory) {
        if (frames == null || cType == null || memory == null) {
            throw new NullPointerException("null parameters being passed to QueryHandler.queryHandler");
        }

        if (cType.equals(CMAType.ONE)) {
            icma = new CMA1();
        } else if (cType.equals(CMAType.TWO)) {
            icma = new CMA2();
        }

        queryResults = new ArrayList<>();

        for (AFrame frame : frames) {
            List<QueryResult> latestList = icma.search(frame, memory);
            if (latestList == null) {
                continue;
            }
            for (QueryResult qR : latestList) {
                queryResults.add(qR);
            }
        }
    }

    public String getAnswer() {
        if (queryResults == null || queryResults.isEmpty()) {
            return "no answer was found";
        }

        if (queryResults.size() == 1) {
            return queryResults.get(0).getResult();
        }

        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < queryResults.size(); i++) {
            answer.append("A").append(i).append(":")
                    .append(queryResults.get(i).getResult().replace(',', ' ')).append(", ");
        }
        return answer.substring(0, answer.length()-1);
    }
}