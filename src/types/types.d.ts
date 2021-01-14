declare namespace JsonSchema {
    export interface ModelApiTask {
        id?: number;
        projectId?: number;
        projectName?: string; 
        name?: string;
        creatorId?: number;
        creatorName?: string;
        priority?: string;
        assigneeId?: number;
        assigneeName?: string;
        category?: ModelApiCategory;
        cratedDate?: string; //date-time
        description?: string;
        estimate?: number;
        timeSpent?: number;
        resolverDate?: string;
        runStart?: string;
        runEnd?: string;
        taskStatus?: string
    }
    export interface ModelsApiUser {
        id?: string;
        name?: string;
        displayName?: string;
        username?: string;
        email?: string;
        roles?: string[],
        team?: string
    }

    export interface ModelApiCategory {
        id?: number;
        name?: string;
        createdDate?: string; //date-time
    }

    export interface ModelApiProject {
        id?: number;
        name?: string;
        isActive?: boolean;
        managerId?: number;
        managerName?: string;
        description?: string;
        createdDate?: string; //date-time
        startDate?: string;
        endDate?: string;
    }

    export interface ModelApiUserRole {
        id?: number;
        user?: ModelsApiUser;
        role?: ModelApiRole;
        is_active?: boolean;
    }

    export interface ModelApiRole {
        id?: number;
        label_pt?: string;
        label_eng?: string;
        short_label_pt?: string;
        short_label_eng?: string;
    }

    export interface ModelApiTeam {
        id?: number;
        name?: string;
        manager?: ModelApiUser;
        createdDate?: string; //date-time
    }

    export interface ModelApiSignInData {
        userName?: string;
        localPwd?: string;
    }

    export interface ModelApiLogWork {
        id?: number;
        timeSpend?: number;
        comment?: string;
        taskId?: number;
        taskName?: string;
        userId?: number;
        userDisplayName?: string;
    }
}