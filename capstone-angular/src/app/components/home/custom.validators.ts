import {AbstractControl, ValidationErrors, ValidatorFn} from "@angular/forms";

export const priceValidator = (faceValue?: number): ValidatorFn => {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!faceValue) return null;
    const max = faceValue + (faceValue * 12 / 100);
    const min = faceValue - (faceValue * 12 / 100)
    const isPriceValid = control.value <= max && control.value >= min;
    return isPriceValid ? null : {validPrice: {error: `Price must be in range of ${min} and ${max} values`}};
  };
}
export const quantityValidator = (minQuantity: number): ValidatorFn => {
  return (control: AbstractControl): ValidationErrors | null => {
    if (control.value && control.value % minQuantity == 0) return null;
    if (!control.value) return {required: true}
    return {minQuantity: {error: `Quantity must be a multiple of ${minQuantity}`}}
  };
}

