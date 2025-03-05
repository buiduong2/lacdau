import type { Component } from 'vue'

export interface FormFieldProps {
  name: string
  label?: string
  desc?: string
  isRequired?: boolean
}

export interface FormFieldCheckboxProps extends FormFieldProps {}

export interface FormFieldSelectProps extends FormFieldProps {
  placeholder: string
  values: {
    label: string
    value: string
  }[]
}

export interface FormFieldTextAreaProps extends FormFieldProps {
  placeholder: string
}

export interface FormFieldRadioProps extends FormFieldProps {
  options: {
    value: string
    label: string
  }[]
}

export interface FormFieldInputProps extends FormFieldProps {
  type: string
  placeholder: string
}
export interface FormGroupToggleProps extends FormGroupProps {
  defaultOpen?: boolean
}
export interface FormGroupProps {
  title: string
  desc: string
}

export interface FromFieldComboboxProps {
  name: string
  desc?: string
  label?: string
  isRequired?: boolean
  placeHolder: string
  commandPlaceHolder: string
  commandList: CommandItem[]
}

export interface CommandItem {
  value: string | number
  label: string
}
export interface FormFieldArrayProps {
  name: string
  label: string
  desc?: string
  isRequired?: boolean
  fieldItem?: Component
  prototypeItem?: any
}

export interface FormlayoutProps {
  title: string
}
