terraform {
  backend "s3" {
    bucket  = "task-time-tracker-db-tfstate"
    key     = "task-time-tracker-db/task-time-tracker.tfstate"
    region  = "af-south-1"
    encrypt = true
  }
}
