import React, { ReactNode } from 'react';
import { Button, ButtonProps } from '@material-ui/core';

export const IButton = ({ classes, children, ...props }: { classes?: any, children: ReactNode } & ButtonProps)=> (

    <Button
        classes={{ ...classes, root: `capitalize font-bold text-center ${classes?.root || ''}` }}
        variant="contained"
        color="primary"
        {...props}
    >
        {children}
    </Button>
)
