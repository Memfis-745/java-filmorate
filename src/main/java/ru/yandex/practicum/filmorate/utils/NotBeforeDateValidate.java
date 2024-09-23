package ru.yandex.practicum.filmorate.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotBeforeDateValidate implements ConstraintValidator<NotBeforeDate, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate data, ConstraintValidatorContext constraintValidatorContext) {
        if (data == null) {
            return false;
        }
        return !data.isBefore(MIN_DATE);
    }

    @Override
    public void initialize(NotBeforeDate constraintAnnotation) {
    }
}