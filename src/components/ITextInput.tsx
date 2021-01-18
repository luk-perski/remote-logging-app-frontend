import React from 'react';
import { TextField, TextFieldProps } from '@material-ui/core';
import { Label } from '@material-ui/icons';

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