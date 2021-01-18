import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button, FormControl, InputLabel, Select } from '@material-ui/core';
import React, { ChangeEvent, ReactNode } from 'react';

export const IDialogAssign = ({ task, userToAssignId, openAssignDialog, handleSubmitAssignDialog, handleCloseAssignDialog, handleDialogFieldChange, users, handleAssigneeChange }
    : { task: JsonSchema.ModelApiTask | null, userToAssignId: number, openAssignDialog: boolean, handleSubmitAssignDialog: () => void, handleCloseAssignDialog: () => void, handleDialogFieldChange: (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => void, users: JsonSchema.ModelsApiUser[] | null, handleAssigneeChange: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined }) => {

    return (
        <Dialog open={openAssignDialog} onClose={handleCloseAssignDialog} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-assign">Assign to task</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Assign person to {task?.name}:
       </DialogContentText>
                <div className="flex flex-col m-auto">
                    <FormControl variant="outlined">
                        <InputLabel >Choose person</InputLabel>
                        <Select
                            native
                            label="Assignee"
                            onChange={handleAssigneeChange}
                        >
                            {users?.map((user) => (
                                <option value={user.id}>
                                    {user.name} ({user.id})
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