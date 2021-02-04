import { FormControl, InputLabel, Select } from '@material-ui/core';
import dayjs from 'dayjs';
import React, { ChangeEvent, ReactNode } from 'react';
import { ITextField } from './ITextField';

export const ITaskDetails = ({
        task,
        disabled,
        editable,
        handleFieldChange,
        handlePriorityChange,
        handleAssigneeChange,
        handleProjectChange,
        handleCategoryChange,
        users,
        projects,
        categories
}: {
        task: JsonSchema.ModelApiTask,
        disabled?: boolean,
        editable?: boolean,
        handleFieldChange?: (field: string) => (event: React.ChangeEvent<HTMLInputElement>) => void,
        handlePriorityChange?: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined,
        users?: JsonSchema.ModelsApiUser[] | null,
        handleAssigneeChange?: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined,
        handleProjectChange?: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined,
        projects?: JsonSchema.ModelApiProject[] | null, handleCategoryChange?: ((event: ChangeEvent<{ name?: string | undefined; value: unknown; }>, child: ReactNode) => void) | undefined,
        categories?: JsonSchema.ModelApiCategory[] | null
}) => {
        const humanizeDuration = require("humanize-duration");

        return (
                <div className="flex flex-row flex-wrap pt-10">
                        <div className="flex-col">
                                <ITextField
                                        labelText="Description"
                                        value={task?.description}
                                        maxRows={32}
                                        multiline={true}
                                        fullWidth={true}
                                        disabled={disabled}
                                        onChange={handleFieldChange ? handleFieldChange("description") : () => void 0}
                                />
                                {editable ? (
                                        <FormControl variant="outlined" className="p-4">
                                                <InputLabel >Category</InputLabel>
                                                <Select
                                                        native
                                                        label="Category"
                                                        onChange={handleCategoryChange}
                                                >
                                                        {categories?.map((category) => (
                                                                <option value={category.id}>
                                                                        {category.name} ({category.id})
                                                                </option>
                                                        ))}
                                                </Select>
                                        </FormControl>
                                ) : (
                                                <ITextField
                                                        labelText="Category"
                                                        value={task?.category?.name}
                                                        disabled={disabled}
                                                />
                                        )}
                        </div>
                        <div className="flex flex-col">
                                {editable ?
                                        <FormControl variant="outlined" className="p-4">
                                                <InputLabel className="p-2">Priority</InputLabel>
                                                <Select
                                                        native
                                                        label="Priority"
                                                        value={task.priority}
                                                        onChange={handlePriorityChange}
                                                >
                                                        <option value={"LOW"}>LOW</option>
                                                        <option value={"MEDIUM"}>MEDIUM</option>
                                                        <option value={"HIGH"}>HIGH</option>
                                                </Select>
                                        </FormControl>
                                        : <ITextField
                                                labelText="Priority"
                                                value={task?.priority}
                                                disabled={disabled}
                                        />}

                                {editable ?
                                        <FormControl variant="outlined" className="p-4">
                                                <InputLabel >Assignee</InputLabel>
                                                <Select
                                                        native
                                                        label="Assignee"
                                                        onChange={handleAssigneeChange}
                                                >
                                                        <option value={-1}>
                                                        </option>
                                                        {users?.map((user) => (
                                                                <option value={user.id}>
                                                                        {user.displayName} ({user.id})
                                                                </option>
                                                        ))}
                                                </Select>
                                        </FormControl>
                                        :
                                        <ITextField
                                                labelText="Assignee"
                                                value={task?.assigneeName}
                                                disabled={disabled}
                                        />
                                }
                                {editable ? null :
                                        <ITextField
                                                labelText="Reporter"
                                                value={task?.creatorName}
                                                disabled={disabled}
                                        />}
                                {editable ?
                                        <FormControl variant="outlined" className="p-4">
                                                <InputLabel >Project</InputLabel>
                                                <Select
                                                        native
                                                        label="Project"
                                                        onChange={handleProjectChange}
                                                >
                                                        {projects?.map((project) => (
                                                                <option value={project.id}>
                                                                        {project.name} ({project.id})
                                                                </option>
                                                        ))}
                                                </Select>
                                        </FormControl>
                                        :
                                        <ITextField
                                                labelText="Project"
                                                value={task?.projectName}
                                                disabled={disabled}
                                        />
                                }
                                {editable ? null :
                                        <ITextField
                                                labelText="Created date"
                                                value={dayjs(task?.cratedDate).format('YYYY-MM-DD HH:mm:ss')}
                                                disabled={disabled}
                                        />}
                        </div>
                        <div className="flex flex-col">
                                <ITextField
                                        labelText="Estimate"
                                        value={String(humanizeDuration(task?.estimate))}
                                        // disabled={disabled}
                                        disabled={true} //todo
                                />
                                {task?.timeSpent ?
                                        <ITextField
                                                labelText="Time spent"
                                                value={task?.timeSpent ? String(humanizeDuration(task?.timeSpent)) : "No time logged"}
                                                disabled={disabled}
                                        /> : null}
                                {task?.resolverDate ? (
                                        <ITextField
                                                labelText="Resolved date"
                                                value={dayjs(task?.resolverDate).format('YYYY-MM-DD HH:mm:ss')}
                                                disabled={disabled}
                                        />) : (
                                                <>
                                                        {task?.runStart ? (
                                                                <ITextField
                                                                        labelText="Last start date"
                                                                        value={dayjs(task?.runStart).format('YYYY-MM-DD HH:mm:ss')}
                                                                        disabled={disabled} />
                                                        ) : null
                                                        }
                                                        {task?.runEnd ?
                                                                (<ITextField
                                                                        labelText="Last end date"
                                                                        value={dayjs(task?.runEnd).format('YYYY-MM-DD HH:mm:ss')}
                                                                        disabled={disabled}
                                                                />
                                                                ) : null
                                                        }
                                                </>
                                        )
                                }
                        </div>
                </div>
        );
}