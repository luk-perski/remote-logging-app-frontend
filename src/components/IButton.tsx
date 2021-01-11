import React, { ReactNode } from 'react';
import { Button, ButtonProps } from '@material-ui/core';

export const IButton = ({ classes, children, isSecondary, ...props }: { classes?: any, children: ReactNode, isSecondary?: boolean } & ButtonProps)=> (

    <Button
        classes={{ ...classes, root: `capitalize font-bold text-center m-2 ${classes?.root || ''}` }}
        variant="contained"
        color={isSecondary? "secondary" : "primary"}
        {...props}
    >
        {children}
    </Button>
)
