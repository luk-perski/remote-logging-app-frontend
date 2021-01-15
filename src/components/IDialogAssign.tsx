import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from '@material-ui/core';
import React from 'react';
import { ITextField } from './ITextField';

export const IDialogAssign = ({ task, userToAssignId, openAssignDialog, handleSubmitAssignDialog, handleCloseAssignDialog, handleDialogFieldChange }
    : { task: JsonSchema.ModelApiTask | null, userToAssignId: number, openAssignDialog: boolean, handleSubmitAssignDialog: () => void, handleCloseAssignDialog: () => void, handleDialogFieldChange: (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => void}) => {

    return (
        <Dialog open={openAssignDialog} onClose={handleCloseAssignDialog} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-assign">Assign to task</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Assign person to {task?.name}:
       </DialogContentText>
                {/* todo change to chose user from team */}
                <div className="flex flex-col w-1/2 m-auto">
                    <ITextField
                        className={"m-1"}
                        labelText="Choose person"
                        type="number"
                        value={userToAssignId > -1 ? userToAssignId : undefined}
                        onChange={handleDialogFieldChange("userToAssignId")}
                    />
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