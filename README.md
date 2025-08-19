# jubilant-happiness
Playing with microservices, Docker, and EKS

---

## ✅ Pre-Requisites Done in AWS

### A) IAM Role (e.g., `GitHubActionsECRRole`)
- An identity that other principals (here GitHub) can assume to get temporary credentials.
- Has no power by itself until you add **policies**.

### B) Trust Policy (attached to the role)
- Defines **who** may assume this role and under what conditions.
- In your case:
  - **Principal**: GitHub’s OIDC provider (`token.actions.githubusercontent.com`)
  - **Conditions**:  
    - Repository: `jimmy23782/jubilant-happiness`  
    - Audience: `sts.amazonaws.com`  
    - Branch restricted to `feature/*`  
- 🔑 Note: Trust and permissions are **separate**. Trust defines *who can assume*, permissions define *what they can do*.

### C) Permissions Policy
- Defines **what AWS actions** the role can perform once assumed.
- For ECR use case:
  - `ecr:PutImage`, `ecr:UploadLayerPart`, etc., scoped to your repo ARN.
- Options:
  - **Managed policy** → `AmazonEC2ContainerRegistryPowerUser` (quick, broader)  
  - **Inline/custom policy** → Tighter, scoped only to your repo.

---

## ⚙️ Minimal Setup in Repo
- **Role**: `GitHubActionsECRRole`  
- **Trust policy**: Only GitHub Actions from your repo/branches can assume it (OIDC conditions).  
- **Permissions policy**: Only allows pushing to your ECR repo (or creating it if missing).  

---

## 🧾 Sanity Checklist

### Trust Policy
- ✅ `aud: sts.amazonaws.com`  
- ✅ `repository: jimmy23782/jubilant-happiness`  
- ✅ `repository_owner: jimmy23782`  
- ✅ `sub` restricted to `feature/*` (extend to `main`/tags later if needed)  

### Permissions Policy
- ✅ Scoped to your **ECR repo ARN** (not `*`)  
- ✅ Includes `ecr:GetAuthorizationToken` on `*` (must be global)  
- ✅ Includes push-related actions on your repo ARN  

### GitHub Actions Job
- ✅ Permissions:  
  ```yaml
  permissions:
    id-token: write
    contents: read