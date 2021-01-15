import React from 'react';
import { TextField, TextFieldProps } from '@material-ui/core';
import { Label } from '@material-ui/icons';

export const ITextField = ({ value, labelText, maxRows, disabled: disabled, onChange, ...props }: { value?:number| string , labelText: string, maxRows?: number, disabled?: boolean, onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void } & TextFieldProps) => (
    <TextField
        className="p-4"
        label={labelText}
        variant="outlined"
        {...props}
        rowsMax={maxRows}
        value={value || typeof value === 'number'  ? value : " "}
        onChange={onChange}
        disabled={disabled}
    />
);