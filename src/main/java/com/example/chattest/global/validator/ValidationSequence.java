package com.example.chattest.global.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;


@GroupSequence({Default.class, ValidationGroups.NotEmptyGroup.class, ValidationGroups.PatternCheckGroup.class})
public interface ValidationSequence {
    /*
    default - NotEmpty - PatternCheck 순서대로 검사
     */
}
