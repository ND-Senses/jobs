package vn.ndm.obj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * User: ADMIN
 * Date: 5/25/2022 - Time: 4:17 PM
 * Project: sync-files
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageVasp2 {
    String ocsCmd;
    String serviceNumber;
    String commandCode;
    String commandArg;
    String listSucc;
}

