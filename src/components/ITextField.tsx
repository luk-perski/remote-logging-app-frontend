import React from 'react';
import { TextField, TextFieldProps } from '@material-ui/core';
import { Label } from '@material-ui/icons';

export const ITextField = ({ value, labelText, maxRows, ...props }: { value?: string, labelText: string, maxRows?: number } & TextFieldProps) => (
    <TextField
        className="p-4"
        disabled
        label={labelText}
        variant="outlined"
        {...props}
        rowsMax={maxRows}
        value={value ? value : " "}
    />
);