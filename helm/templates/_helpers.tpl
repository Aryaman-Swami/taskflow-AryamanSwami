{{/*
Expand the name of the chart.
*/}}
{{- define "taskflow.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
Truncated to 63 chars — Kubernetes label limit.
*/}}
{{- define "taskflow.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart label.
*/}}
{{- define "taskflow.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels applied to every resource.
*/}}
{{- define "taskflow.labels" -}}
helm.sh/chart: {{ include "taskflow.chart" . }}
{{ include "taskflow.selectorLabels" . }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels — used by Service and Deployment to match pods.
*/}}
{{- define "taskflow.selectorLabels" -}}
app.kubernetes.io/name: {{ include "taskflow.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Service account name.
*/}}
{{- define "taskflow.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "taskflow.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Bitnami PostgreSQL subchart service name: <release>-postgresql
*/}}
{{- define "taskflow.postgresHost" -}}
{{- printf "%s-postgresql" .Release.Name }}
{{- end }}
