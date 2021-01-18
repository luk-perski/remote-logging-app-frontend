import { Breadcrumbs, Link, Typography } from '@material-ui/core';
import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { IButton } from '../components/IButton';
import { ITaskDetails } from '../components/ITaskDetails';
import { ITextField } from '../components/ITextField';
import { PageTitle } from '../components/PageTitle';
import { getProjects } from '../store/actions/projects';
import { handleSetTaskField, addTask, setReturnToTasks, setTaskToAdd, getUsers } from '../store/actions/tasks';
import { RootState } from '../store/reducers';
import { pages } from '../utils/pages';

//todo add Snackbars
export const AddTask = () => {

    const dispatch = useDispatch();
    const state = useSelector((state: RootState) => state);
    const Lockr = require("lockr");
    const tasks = state.tasks
    const project = state.projects
    const projects = project.projectsList
    const task = tasks.taskToAdd
    const redirect = tasks.isReturnToTasks
    const users = tasks.users

    useEffect(() => {
        dispatch(setReturnToTasks(false))
        //todo ask why useEffect works like this
        console.log("Use effect:")
        console.log(redirect)
        dispatch(getUsers());
        dispatch(getProjects())
    }, [dispatch]);

    const handleAddButton = () => {
        console.log(users)
        dispatch(addTask(task));
    }

    const handleChange = (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log(task)
        dispatch(handleSetTaskField(field, event.target.value, task));
    };

    const handlePriorityChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        dispatch(handleSetTaskField("priority", event.target.value as string, task));
    };

    const handleAssigneeChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        dispatch(handleSetTaskField("assignee", event.target.value as string, task));
    };

    const handleProjectChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        dispatch(handleSetTaskField("project", event.target.value as string, task));
    };

    const handleRedirect = (value: boolean) => {
        dispatch(setReturnToTasks(value))
    }

    if (redirect) {
        console.log("If:")
        console.log(redirect)
        return <Redirect push to={pages.tasks.url()} />
    }
    return (
        <>
            <PageTitle title="Add task" />
            <div className="flex-col">
                <Breadcrumbs aria-label="breadcrumb" className="mt-3">
                    <Link color="inherit"
                        onClick={() => handleRedirect(true)}>
                        Tasks
                </Link>
                    <Typography color="textPrimary">Add task</Typography>
                </Breadcrumbs>
                <div className="flex-col p-6">
                    <Typography variant="h5">Add task</Typography>
                    <ITextField
                        className="mt-8 ml-4"
                        labelText="Name"
                        fullWidth={true}
                        value={task.name}
                        onChange={handleChange("name")}
                    />
                    <ITaskDetails 
                    task={task} 
                    editable={true} 
                    handleFieldChange={handleChange}
                     handlePriorityChange={handlePriorityChange} 
                     users={users} 
                     handleAssigneeChange={handleAssigneeChange}
                     projects={projects}
                     handleProjectChange={handleProjectChange}
                     />
                </div>
                <IButton onClick={() => handleAddButton()}>Add</IButton>
            </div>
        </>
    );
}