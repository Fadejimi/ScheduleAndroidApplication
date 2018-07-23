package app.rest.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("tasksMilestones")
    @Expose
    private List<TasksMilestone> tasksMilestones = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<TasksMilestone> getTasksMilestones() {
        return tasksMilestones;
    }

    public void setTasksMilestones(List<TasksMilestone> tasksMilestones) {
        this.tasksMilestones = tasksMilestones;
    }


    public double getPercentage() {
        if (tasksMilestones != null) {
            double percentage = 0;
            for (TasksMilestone milestone : tasksMilestones) {
                percentage += milestone.getPercentage();
            }
            return percentage/tasksMilestones.size();
        }
        return 0;
    }

    public String toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", getId());
            object.put("name", getName());
            object.put("description", getDescription());

            return object.toString();
        }
        catch(JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
