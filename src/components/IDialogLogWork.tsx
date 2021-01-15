import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from '@material-ui/core';
import React from 'react';
import { ITextField } from './ITextField';

export const IDialogLogWork = ({ task, days, hours, minutes, logWorkComment, openLogDialog, handleDialogFieldChange, handleCloseLogDialog, handleSubmitLogDialog }: { task: JsonSchema.ModelApiTask | null, days: number, hours: number, minutes: number,logWorkComment: string, openLogDialog: boolean, handleDialogFieldChange: (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => void, handleCloseLogDialog: () => void, handleSubmitLogDialog: () => void }) => {
    return (
        <Dialog open={openLogDialog} onClose={handleCloseLogDialog} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-log-work">Log work</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Log time to task {task?.name}:
           </DialogContentText>
                {/* todo add nicer pickers */}
                <div className="flex flex-col w-1/3 m-auto">
                    <ITextField
                        className={"m-1"}
                        labelText="Days"
                        type="number"
                        value={days}
                        onChange={handleDialogFieldChange("days")
                        }
                    />
                    <ITextField
                        className={"m-1"}
                        labelText="Hours"
                        type="number"
                        value={hours}
                        onChange={handleDialogFieldChange("hours")}

                    />
                    <ITextField
                        className={"m-1"}
                        labelText="Minutes"
                        type="number"
                        value={minutes}
                        onChange={handleDialogFieldChange("minutes")}
                    />
                </div>
                <ITextField
                    className={"m-1"}
                    labelText="Comment"
                    value={logWorkComment}
                    onChange={handleDialogFieldChange("logWorkComment")}
                    maxRows={32}
                    fullWidth={true}
                    multiline={true}
                />
                {/* <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <TimePicker
                onChange={void 0}
                value={value}
            />
            </MuiPickersUtilsProvider> */}
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCloseLogDialog} color="primary">
                    Cancel
            </Button>
                <Button onClick={handleSubmitLogDialog} color="primary">
                    Submit
          </Button>
            </DialogActions>
        </Dialog>
    );
}