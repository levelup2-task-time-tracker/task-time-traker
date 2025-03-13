
output "task_time_tracker_db_endpoint" {
  value = aws_db_instance.task_time_tracker_instance.endpoint
}

output "task_time_tracker_db_address" {
  value = aws_db_instance.task_time_tracker_instance.address
}

output "task_time_tracker_db_port" {
  value = aws_db_instance.task_time_tracker_instance.port
}
