#!/bin/bash

# Initialize git repository
git init

# Add all files to staging
git add -A

# Prompt user for commit message
echo "Enter your commit message:"
read commit_message

# Commit changes with the provided message
git commit -m "$commit_message"

# Prompt user for commit message
echo "Enter your Repo link (Ex - Demo Project.git) "
read repo_link

# Add remote origin
git remote add origin git@github.com:xCodeFactor/$repo_link

# Prompt user for branch
echo "Enter branch"
read branch

# Push to main branch with force flag
git push -u -f origin $branch

echo "Git setup and push completed!"