package com.example.godtudy.global.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

import static com.example.godtudy.global.validation.ValidationGroups.*;

@GroupSequence({Default.class, NotEmptyGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {
    /*
    default - NotEmpty - PatternCheck 순서대로 검사
     */
}
