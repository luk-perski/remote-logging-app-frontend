import React from 'react';
import { TextField, TextFieldProps } from '@material-ui/core';

export const ITextInput = ({ value, labelText, maxRows, type, ...props}: {value?: string | number,  labelText: string, maxRows?: number, type?: string } &TextFieldProps) => (
    <TextField
        type={type? type : "text"}
        label={labelText}
        variant="outlined"
        {...props} 
        rowsMax={maxRows}
        value={value}
    />
);