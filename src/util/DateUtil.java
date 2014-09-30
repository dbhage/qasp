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

package util;

import java.util.Date;

/**
 * DateUtil class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 11:34:41 PM, Nov 20, 2013 
 * Description: utility functions pertinent to date
 */
public class DateUtil {

    /**
     * Takes a long and returns a date in format dd-mmm-yyyy hh:mm dd is 1 or 2
     * digits representing the day mmm is three letters representing the month
     * yyyy is 4 digits representing the year hh:mm the time (hour and minute)
     * Example: 1385347724573 -> 24-Nov-2013 21:48
     *
     * @param date - <code>long</code> representing the time in ms from the
     * epoch
     * @return <code>String</code> representing date and time
     */
    public static String dateToString(long date) {
        String[] split = (new Date(date)).toString().split(" ");
        return split[2] + "-" + split[1] + "-" + split[5] + " " + split[3].substring(0, 5);
    }
}