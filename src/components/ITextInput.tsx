import React from 'react';
import { TextField, TextFieldProps } from '@material-ui/core';
import { Label } from '@material-ui/icons';

export const ITextInput = ({ value, labelText, maxRows, type, ...props}: {value?: string,  labelText: string, maxRows?: number, type?: string } &TextFieldProps) => (
    <TextField
        // required
        type={type? type : "text"}
        // fullWidth
        // multiline
        label={labelText}
        // autoComplete="current-password"
        // helperText="Some important text"
        variant="outlined"
        {...props} 
        rowsMax={maxRows}
        // onChange={handleChange}
        value={value}
    // InputProps={{
    //     readOnly: true,
    // }}
    />
);