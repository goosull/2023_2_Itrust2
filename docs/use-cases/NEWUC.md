# UC23 Payments

## 23.1 Preconditions

- An HCP and a patient are registered in the iTrust2 system.
- An iTrust2 user is logged into the system.

## 23.2 Main Flow

The HCP selects the patient and generates an invoice for payment.

The patient reviews the invoice and makes payment.

The HCP and the patient view a table of invoices and their payment status.

## 23.3 Sub-flows

- [S1] The HCP selects the patient.
- [S2] The HCP enters the information including service dates and total cost.
- [S3] The HCP generates an invoice.
- [S4] The patient reviews and pays the invoice.
- [S5] The HCP and the patient view a table of invoices and their payment status.

## 23.4 Alternative Flows

- [E1] An error message is displayed if the HCP enters invalid data.
- [E2] If the HCP or the patient have no invoices, the system displays an error message.

## 23.5 Logging

| Transaction Code | Verbose Description | Logged In MID  | Secondary MID | Transaction Type | Patient Viewable |
| ---------------- | ------------------- | -------------- | ------------- | ---------------- | ---------------- |
| 2300             | Generate invoice    | HCP            | Patient       | Create           | Yes              |
| 2301             | Process payment     | Patient        | HCP           | Edit             | Yes              |
| 2302             | View invoices       | HCP or Patient | None          | View             | Yes              |

## 23.6 Data Format

### Invoice

| Field   | Format                |
| ------- | --------------------- |
| hcp     | Reference to HCP      |
| patient | Reference to patient  |
| start   | Month Day, Year       |
| end     | Month Day, Year       |
| cost    | Positive integer      |
| status  | 1 - Pending; 2 - Paid |

## 23.7 Acceptance Scenarios

**Scenario 1:: Generate an invoice**

HCP Shelly Vang authenticates into iTrust2. Dr. Vang generates an invoice of medical services provided for Brynn McClain from October 1, 2017 to October 10, 2017, which cost $14728 in total. She enters the information and clicks submit. A message notifies her that the entry was recorded successfully.

**Scenario 2:: Review and pay the invoice**

Brynn McClain authenticates into iTrust2. He clicks "View invoices". He is presented with a list of invoices. The table contains one entry: HCP: Shelly Vang, Start: October 1, 2017, End: October 10, 2017, Cost: $14728, Status: 1(Pending). Brynn clicks the pay button next to the entry, then the status of the entry updates to 2(Paid).

**Scenario 3:: View invoices**

HCP Shelly Vang authenticates into iTrust2. She clicks "View invoices". She is presented with a list of invoices. The table contains one entry: Patient: Brynn McClain, Start: October 1, 2017, End: October 10, 2017, Cost: $14728, Status: 2(Paid).
