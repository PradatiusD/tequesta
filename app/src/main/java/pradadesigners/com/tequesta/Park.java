package pradadesigners.com.tequesta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dprada on 12/25/16.
 */

public class Park {

    private String mStatus;
    private String mName;
    private String mLastUpdated;
    private String mComment;


    public Park (String parkHTML) {

        Pattern pattern = Pattern.compile("<td((\\S|\\s)*?)</td>");
        Matcher matcher = pattern.matcher(parkHTML);

        while (matcher.find()) {
            String match = matcher.group(1);
            match = match.replaceAll("(\\n|\\r|\\t)","");
            if (match.contains("statusImage")) {
                String parkStatus = match.contains("cell-14") ? "open" : "closed";
                mStatus = parkStatus;
            }
            else if (match.contains("span")) {
                match = match.split("<br")[0];
                match = match.replaceAll("(\\s{2,}|>)","");
                mName = match;
            }
            else if (match.contains(">Updated")) {
                match = match.replace(" align=\"center\" width=\"150\">Updated ","");
                mLastUpdated = match;
            }
            else if (match.contains("align")) {
                match = match.replace(" align=\"center\" width=\"125\">","");
                match = match.replaceAll("\\s{2,}","");
                match = match.replace("<br />",": ");
                mComment = match;
            }
        }
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        mLastUpdated = lastUpdated;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    @Override
    public String toString () {
        return mName + "\n" + mStatus + "\n" + mLastUpdated + "\n" + mComment;
    }


}
