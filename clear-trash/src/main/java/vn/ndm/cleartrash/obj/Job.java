package vn.ndm.cleartrash.obj;

import lombok.Data;

import java.util.List;

@Data
public class Job {
    private String name;
    private List<String> paths;

    public Job(String name, List<String> paths) {
        this.name = name;
        this.paths = paths;
    }
}

