import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, FormControl, InputLabel, Select } from '@material-ui/core';
import React, { ChangeEvent, ReactNode } from 'react';
import { ITextField } from './ITextField';

export const IDialogAssign = ({ task, userToAssignId, openAssignDialog, handleSubmitAssignDialog, handleCloseAssignDialog, handleDialogFieldChange, users, handleAssigneeChange }
    : { task: JsonSchema.ModelApiTask | null, userToAssignId: number, openAssignDialog: boolean, handleSubmitAssignDialog: () => void, handleCloseAssignDialog: () => void, handleDialogFieldChange: (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => void, users: JsonSchema.Option[] | null, handleAssigneeChange: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined}) => {

    return (
        <Dialog open={openAssignDialog} onClose={handleCloseAssignDialog} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-assign">Assign to task</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Assign person to {task?.name}:
       </DialogContentText>
                {/* todo change to chose user from team */}
                <div className="flex flex-col m-auto">
                    {/* <ITextField
                        className={"m-1"}
                        labelText="Choose person"
                        type="number"
                        value={userToAssignId > -1 ? userToAssignId : undefined}
                        onChange={handleDialogFieldChange("userToAssignId")}
                    /> */}
                    <FormControl variant="outlined">
                        <InputLabel >Choose person</InputLabel>
                        <Select
                            native
                            label="Assignee"
                            onChange={handleAssigneeChange}
                        >
                            {users?.map((user) => (
                                <option value={user.value}>
                                    {user.label} ({user.value})
                                </option>
                            ))}
                        </Select>
                    </FormControl>
                </div>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCloseAssignDialog} color="primary">
                    Cancel
        </Button>
                <Button onClick={handleSubmitAssignDialog} color="primary">
                    Assign
      </Button>
            </DialogActions>
        </Dialog>
    );
}