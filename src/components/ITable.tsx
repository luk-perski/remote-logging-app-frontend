import React, { ReactNode, MouseEventHandler } from 'react';
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';

export const IHeaderCell = ({ children, className, align }: { children: ReactNode, className?: string, align?: "left" | "center" | "right" | "justify" | "inherit" })=> (
    <TableCell classes={{ root: `${className || ''}` }} align={align || 'left'}>
        {children}
    </TableCell>
);

export const IHeader = ({ headers, className }: { headers: string[], className?: string })=> (
    <TableHead classes={{ root: `${className || ''}` }}>
        <TableRow>
            {headers.map((header, i)=> (
                <IHeaderCell className="font-bold" key={header}>
                    {header}
                </IHeaderCell>
            ))}
        </TableRow>
    </TableHead>
);

export const ICell = ({ children, className }: { children: ReactNode, className?: string })=> (
    <TableCell classes={{ root: `${className || ''}` }}>
        {children}
    </TableCell>
)

export const IBody = ({ children, className }: { children: ReactNode, className?: string })=> (
    <TableBody classes={{ root: `${className || ''}` }}>
        {children}
    </TableBody>
);

export const IRow = ({ children, onClick, className }: { children: ReactNode, onClick?: MouseEventHandler, className?: string })=> (
    <TableRow
        classes={{ root: `${className || ''} ${onClick? 'cursor-pointer' : ''}` }}
        onClick={onClick? onClick : ()=> void 0}
        hover={!!onClick}
    >
        {children}
    </TableRow>
)

export const IDetailRow = ({ header, className, children }: { header: string, children: ReactNode, className?: string })=> {
    return (
        <TableRow>
            <TableCell classes={{ root: 'font-bold' }}>
                {header}
            </TableCell>
            <TableCell classes={{ root: `${className || ''}` }}>
                {children}
            </TableCell>
        </TableRow>
    )
}

export const ITable = ({ children, className, component }: { children: ReactNode, className?: string, component?: any })=> (
    <div className={className}>
        <TableContainer component={component || Paper}>
            <Table classes={{ root: `${className || ''}` }}>
                {children}
            </Table>
        </TableContainer>
    </div>
);
