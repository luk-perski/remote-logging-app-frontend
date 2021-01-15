import { CircularProgress, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, Input, InputLabel, TextField } from '@material-ui/core';
import { Button } from '@material-ui/core';
import { Typography } from '@material-ui/core';
import { useTheme, useMediaQuery } from '@material-ui/core';
import dayjs from 'dayjs';
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { IButton } from '../components/IButton';
import { ITextField } from '../components/ITextField';
import { PageTitle } from '../components/PageTitle';
import { getTask, handleSetDialogField, setOpenLogDialog as setOpenLogDialog, assignUser, startProgress, suspendTask, setOpenAssignDialog, addLogWork } from '../store/actions/tasks';
import { RootState } from '../store/reducers';
import { ITextInput } from '../components/ITextInput';
import { USER_ID } from '../utils/lockrKeys';
import { ITaskDetails } from '../components/ITaskDetails';
import { IDialogLogWork } from '../components/IDialogLogWork';
import { IDialogAssign } from '../components/IDialogAssign';

interface RouteParams {
    taskId: string
}

export const TaskDetails = () => {
    const dispatch = useDispatch();
    const params = useParams<RouteParams>();
    const state = useSelector((state: RootState) => state);
    const theme = useTheme();
    const matches = useMediaQuery(theme.breakpoints.up('md'));
    const humanizeDuration = require("humanize-duration");
    const Lockr = require("lockr");

    const tasks = state.tasks
    const userId = Lockr.get(USER_ID)     //change it!!!
    const task = tasks.selectedTaskRequest;
    const loadingTaskRequest = tasks.loadingTaskRequest;
    const taskId = Number(params.taskId)
    const logWork = tasks.logWorkToAdd;
    const days = tasks.logWorkDays;
    const hours = tasks.logWorkHours;
    const minutes = tasks.logWorkMinutes;
    const userToAssignId = tasks.userToAssignId;
    const logWorkComment = tasks.logWorkComment;

    if (!loadingTaskRequest && (!task || task.id !== taskId)) {
        dispatch(getTask(taskId));
    }

    // const [open, setOpen] = React.useState(false);
    const openLogDialog = tasks.openLogDialog;
    const openAssignDialog = tasks.openAssignDialog;

    const handleClickLogDialogOpen = () => {
        dispatch(setOpenLogDialog(true));
    };

    const handleCloseLogDialog = () => {
        dispatch(setOpenLogDialog(false));
    };


    const handleSubmitLogDialog = () => {
        dispatch(setOpenLogDialog(false));

        console.log("comment: " + logWorkComment)
        dispatch(addLogWork(taskId, userId, days, hours, minutes, logWorkComment));
    };

    const handleClickAssignDialogOpen = () => {
        dispatch(setOpenAssignDialog(true));
    };

    const handleCloseAssignDialog = () => {
        dispatch(setOpenAssignDialog(false));
    };


    const handleSubmitAssignDialog = () => {
        dispatch(setOpenAssignDialog(false));

        dispatch(assignUser(taskId, userToAssignId));
    };

    // ask why I need use there dispatch()
    const handleDialogFieldChange = (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
        dispatch(handleSetDialogField(field, event.target.value));
    };

    const handleAssign = () => {
        assignUser(taskId, 4)
    }

    //todo ask Artur why I need here dispatch 
    const handleStartProgress = () => {
        dispatch(startProgress(taskId, userId));
    }

    const handleSupspend = () => {
        dispatch(suspendTask(taskId, userId));
    }

    return (
        <>
            <PageTitle title={`Task - ${task?.name}`} />
            {loadingTaskRequest ? (
                <div className="ml-64 mt-6">
                    <CircularProgress />
                </div>
            ) : (
                    <>
                        <div className="flex-col p-6">
                            <div className="flex">
                                <Typography variant="h5">{task?.name}</Typography>
                                <Typography className={"m-1"} variant="button">{task?.taskStatus}</Typography>
                            </div>
                            <div className="py-3">
                                <IButton onClick={handleClickAssignDialogOpen}>Assign</IButton>
                                <IButton onClick={handleClickLogDialogOpen}>Log work</IButton>
                                {userId === task?.assigneeId ?
                                    <>
                                        {/* ask why I have logs after login, TabsBar, App in cosole after click buttons */}
                                        {task?.taskStatus === "NEW" || task?.taskStatus === "SUSPEND" ?
                                            <IButton onClick={handleStartProgress}>Start progress</IButton>
                                            : <IButton onClick={handleSupspend} isSecondary={true}>Supspend</IButton>}
                                    </>
                                    : null}

                            </div>
                            <ITaskDetails task={task} disabled={true} />
                        </div>
                        <IDialogLogWork
                            task={task}
                            days={days}
                            hours={hours}
                            minutes={minutes}
                            logWorkComment={logWorkComment}
                            handleCloseLogDialog={handleCloseLogDialog}
                            handleDialogFieldChange={handleDialogFieldChange}
                            handleSubmitLogDialog={handleSubmitLogDialog}
                            openLogDialog={openLogDialog}
                        />
                        <IDialogAssign
                        task={task}
                        userToAssignId={userToAssignId}
                        openAssignDialog={openAssignDialog}
                        handleCloseAssignDialog={handleCloseAssignDialog}
                        handleSubmitAssignDialog={handleSubmitAssignDialog}
                        handleDialogFieldChange={handleDialogFieldChange}
                        />
                    </>
                )
            }
        </>
    )
}