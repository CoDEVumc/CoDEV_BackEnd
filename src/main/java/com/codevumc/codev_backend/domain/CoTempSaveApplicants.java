package com.codevumc.codev_backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoTempSaveApplicants {
    List<String> co_emails;

    public boolean checkAllTempSave(List<Boolean> coTempSaveStatus) {
        for (int i = 1; i < coTempSaveStatus.size(); i++) {
            if (coTempSaveStatus.get(i) != coTempSaveStatus.get(0)) {
                return false;
            }
        }
        return true;
    }

}
