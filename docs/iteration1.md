# Iteration 1

## Team Members

| Student ID | Name   | GitLab Username |
| ---------- | ------ | --------------- |
| 2022148004 | 이협   | dlguq0107       |
| 2018122053 | 노우준 | 2018122053      |
| 2022148047 | 최우현 | starwh          |
| 2021147562 | 김이주 | 2021147562      |
| 2022148082 | 김승원 | goosull         |
| 2018122018 | 박태주 | taejoo          |
| 2021123062 | 유광수 | YuKwangsu       |
| 2021182012 | 손예원 | SonYewon        |
| 2018122021 | 최병준 | ByungJune       |

## Leaders

- Team Leader: 노우준
- QA Leader: 이협
- Planning Leader: 최우현

## Use Cases

### New UC: Payments

#### Motivation

Billing is an essential part of medical and insurance services.

#### User Story

- **HCP** wants to be able to generate an invoice to charge the patient for care.
- **Patient** wants to be able to check the invoice and pay for it.
- **HCP** and **patients** want to be able to see their invoicing and payment status in order to know their previous invoicing and payment status.

#### Preconditions

- An HCP and a patient are registered in the iTrust2 system.
- An iTrust2 user is logged into the system.
- The HCP must have begun documenting an office visit.

#### Main Flow

The HCP selects the patient and generates an invoice for payment.

The patient reviews the invoice and makes payment.

The HCP and the patient view a table of invoices and their payment status.

#### Sub-flows

- [S1] The HCP selects the patient.
- [S2] The HCP enters the information including service dates and total cost.
- [S3] The HCP generates an invoice.
- [S4] The patient reviews and pays the invoice.
- [S5] The HCP and the patient view a table of invoices and their payment status.

#### Alternative Flows

- [E1] An error message is displayed if the HCP enters invalid data.
- [E2] If the HCP or the patient have no invoices, the system displays an error message.

#### Logging

| Transaction Code | Verbose Description | Logged In MID  | Secondary MID | Transaction Type | Patient Viewable |
| ---------------- | ------------------- | -------------- | ------------- | ---------------- | ---------------- |
| 2300             | Generate invoice    | HCP            | Patient       | Create           | Yes              |
| 2301             | Process payment     | Patient        | HCP           | Edit             | Yes              |
| 2302             | View invoices       | HCP or Patient | None          | View             | Yes              |

#### Data Format

- **Invoice**

| Field   | Format                |
| ------- | --------------------- |
| hcp     | Reference to HCP      |
| patient | Reference to patient  |
| start   | Month Day, Year       |
| end     | Month Day, Year       |
| cost    | Positive integer      |
| status  | 1 - Pending; 2 - Paid |

#### Acceptance Scenarios

**Scenario 1:: Generate an invoice**

HCP Shelly Vang authenticates into iTrust2. Dr. Vang generates an invoice of medical services provided for Brynn McClain from October 1, 2017 to October 10, 2017, which cost $14728 in total. She enters the information and clicks submit. A message notifies her that the entry was recorded successfully.

**Scenario 2:: Review and pay the invoice**

Brynn McClain authenticates into iTrust2. He clicks "View invoices". He is presented with a list of invoices. The table contains one entry: HCP: Shelly Vang, Start: October 1, 2017, End: October 10, 2017, Cost: $14728, Status: 1(Pending). Brynn clicks the pay button next to the entry, then the status of the entry updates to 2(Paid).

**Scenario 3:: View invoices**

HCP Shelly Vang authenticates into iTrust2. She clicks "View invoices". She is presented with a list of invoices. The table contains one entry: Patient: Brynn McClain, Start: October 1, 2017, End: October 10, 2017, Cost: $14728, Status: 2(Paid).

### UC15

- user story
- main flow (간략하게)

### UC16

- user story
- main flow (간략하게)

### UC20

- user story
- main flow (간략하게)

### UC21

#### User Story

- **Ophthalmologist and optometrist HCP** wants to be able to enter eye health information to record the patient's eye health status.
- **Ophthalmologist and optometrist HCP** wants to be able to view or edit the diagnosis of the practice to manage and update the patient's diagnostic information.

#### Main Flow

### UC22

- user story
- main flow (간략하게)

## Next iteration

### Leaders

- Team Leader: 유광수
- QA Leader: 박태주
- Planning Leader: 김이주

### 작업 분담

- UC15, UC16: 김승원, 손예원, 유광수
- UC20, UC22: 이협, 최병준, 최우현
- UC21, New UC: 김이주, 노우준, 박태주
