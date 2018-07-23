package app.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Schedule {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("links")
    @Expose
    private List<Link> links = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("scheduleMilestone")
    @Expose
    private Integer scheduleMilestone;

    private Double rating;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScheduleMilestone() {
        return scheduleMilestone;
    }

    public void setScheduleMilestone(Integer scheduleMilestone) {
        this.scheduleMilestone = scheduleMilestone;
        double rate = scheduleMilestone/100 * 5;
        setRating(rate);
    }

    public void setRating(double rate) {
        this.rating = rate;
    }

    public double getRating() {
        return this.rating;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());
            jsonObject.put("description", getDescription());
            jsonObject.put("scheduleMilestone", getScheduleMilestone());

            return jsonObject.toString();
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}